package com.payment.fabriciolfj.problem.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Stream;

import static com.payment.fabriciolfj.problem.error.ErrorEnum.VALIDATION_ERROR;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERRORS = "errors";
    private static final String CODE = "code";
    private static final String HEADER_REQUIRED = "header.required";

    private final MessageSource messages;

    public record FieldErrorResponse(String field, String message) {
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatusCode status,
                                                                  final WebRequest request) {
        final var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(GlobalExceptionHandler::toResponse)
                .toList();

        return ResponseEntity.badRequest().body(problem(errors));
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(final HandlerMethodValidationException ex,
                                                                            final HttpHeaders headers,
                                                                            final HttpStatusCode status,
                                                                            final WebRequest request) {
        final var errors = ex.getParameterValidationResults()
                .stream()
                .flatMap(GlobalExceptionHandler::toResponses)
                .toList();

        return ResponseEntity.badRequest().body(problem(errors));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatusCode status,
                                                                          final WebRequest request) {
        if (ex instanceof MissingRequestHeaderException missing) {
            final var message = messages.getMessage(HEADER_REQUIRED, null, LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(problem(List.of(new FieldErrorResponse(missing.getHeaderName(), message))));
        }
        return super.handleServletRequestBindingException(ex, headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(final ConstraintViolationException ex) {
        final var errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new FieldErrorResponse(lastNode(v.getPropertyPath().toString()), v.getMessage()))
                .toList();

        return problem(errors);
    }

    private static ProblemDetail problem(final List<FieldErrorResponse> errors) {
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, VALIDATION_ERROR.toMessage());
        problem.setProperty(CODE, VALIDATION_ERROR.name());
        problem.setProperty(ERRORS, errors);
        return problem;
    }

    private static Stream<FieldErrorResponse> toResponses(final ParameterValidationResult result) {
        if (result instanceof ParameterErrors errors) {
            return errors.getFieldErrors().stream().map(GlobalExceptionHandler::toResponse);
        }

        final var name = parameterName(result.getMethodParameter());

        return result.getResolvableErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .map(message -> new FieldErrorResponse(name, message));
    }

    private static String parameterName(final MethodParameter parameter) {
        final var header = parameter.getParameterAnnotation(RequestHeader.class);
        if (header != null && !header.name().isBlank()) {
            return header.name();
        }

        final var param = parameter.getParameterAnnotation(RequestParam.class);
        if (param != null && !param.name().isBlank()) {
            return param.name();
        }

        final var path = parameter.getParameterAnnotation(PathVariable.class);
        if (path != null && !path.name().isBlank()) {
            return path.name();
        }

        return parameter.getParameterName();
    }

    private static FieldErrorResponse toResponse(final FieldError error) {
        return new FieldErrorResponse(error.getField(), error.getDefaultMessage());
    }

    private static String lastNode(final String path) {
        final int idx = path.lastIndexOf('.');
        return idx < 0 ? path : path.substring(idx + 1);
    }
}
