package com.payment.fabriciolfj.problem.exception;

import com.payment.fabriciolfj.problem.error.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@Getter
public abstract class BusinessException extends ErrorResponseException {

    private static final String CODE = "code";

    private final ErrorEnum error;

    protected BusinessException(final HttpStatus status, final ErrorEnum error) {
        super(status, problem(status, error), null);
        this.error = error;
    }

    private static ProblemDetail problem(final HttpStatus status, final ErrorEnum error) {
        final var problem = ProblemDetail.forStatusAndDetail(status, error.toMessage());
        problem.setProperty(CODE, error.name());

        return problem;
    }
}
