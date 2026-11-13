package com.payment.fabriciolfj.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.payment.fabriciolfj.constants.MdcConstants;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.payment.fabriciolfj.constants.HeaderConstants.CORRELATION_ID;
import static com.payment.fabriciolfj.constants.HeaderConstants.IDEMPOTENCE_KEY;
import static com.payment.fabriciolfj.constants.HeaderConstants.IDEMPOTENCE_KEY_MAX_SIZE;
import static com.payment.fabriciolfj.constants.HeaderConstants.IDEMPOTENCE_KEY_MIN_SIZE;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        final var correlationId = StringUtils.hasText(request.getHeader(CORRELATION_ID))
                ? request.getHeader(CORRELATION_ID)
                : UUID.randomUUID().toString();
        final var idempotencyKey = request.getHeader(IDEMPOTENCE_KEY);

        try {
            MDC.put(MdcConstants.CORRELATION_ID, correlationId);
            if (isValidIdempotencyKey(idempotencyKey)) {
                MDC.put(MdcConstants.IDEMPOTENCY_KEY, idempotencyKey);
            }
            response.setHeader(CORRELATION_ID, correlationId);

            chain.doFilter(request, response);
        } finally {
            MDC.remove(MdcConstants.CORRELATION_ID);
            MDC.remove(MdcConstants.IDEMPOTENCY_KEY);
        }
    }


    private static boolean isValidIdempotencyKey(final String key) {
        return key != null
                && key.length() >= IDEMPOTENCE_KEY_MIN_SIZE
                && key.length() <= IDEMPOTENCE_KEY_MAX_SIZE;
    }
}
