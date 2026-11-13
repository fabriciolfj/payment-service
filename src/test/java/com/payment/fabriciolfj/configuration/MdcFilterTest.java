package com.payment.fabriciolfj.configuration;

import com.payment.fabriciolfj.constants.MdcConstants;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.concurrent.atomic.AtomicReference;

import static com.payment.fabriciolfj.constants.HeaderConstants.IDEMPOTENCE_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MdcFilterTest {

    private final MdcFilter filter = new MdcFilter();

    private String keyInMdcDuringRequest(final String headerValue) throws Exception {
        final var request = new MockHttpServletRequest();
        if (headerValue != null) {
            request.addHeader(IDEMPOTENCE_KEY, headerValue);
        }
        final var captured = new AtomicReference<String>();
        filter.doFilter(request, new MockHttpServletResponse(),
                (req, res) -> captured.set(MDC.get(MdcConstants.IDEMPOTENCY_KEY)));
        return captured.get();
    }

    @Test
    void shouldPutValidKeyInMdc() throws Exception {
        assertEquals("0123456789abcdef", keyInMdcDuringRequest("0123456789abcdef"));
        assertNull(MDC.get(MdcConstants.IDEMPOTENCY_KEY));
    }

    @Test
    void shouldNotPutInvalidKeyInMdc() throws Exception {
        assertNull(keyInMdcDuringRequest("short"));
        assertNull(keyInMdcDuringRequest("x".repeat(65)));
        assertNull(keyInMdcDuringRequest(null));
    }
}
