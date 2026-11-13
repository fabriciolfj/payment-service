package com.payment.fabriciolfj.problem.handler;

import com.payment.fabriciolfj.entrypoint.controller.ChargeRequest;
import com.payment.fabriciolfj.problem.exception.ConflitIdempotencyException;
import com.payment.fabriciolfj.problem.exception.MoneyInvalidException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.payment.fabriciolfj.constants.HeaderConstants.IDEMPOTENCE_KEY;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.ValidationTestController.class)
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.ValidationTestController.class})
class GlobalExceptionHandlerTest {

    private static final String VALID_KEY = "0123456789abcdef";

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class ValidationTestController {
        @PostMapping("/test/validation")
        void create(@Valid @RequestBody final ChargeRequest request,
                    @RequestHeader(IDEMPOTENCE_KEY) @Size(min = 16, max = 64, message = "{idempotency.key.size}")
                    final String idempotencyKey) {
        }

        @PostMapping("/test/money")
        void money() {
            throw new MoneyInvalidException();
        }

        @PostMapping("/test/conflict")
        void conflict() {
            throw new ConflitIdempotencyException();
        }
    }

    @Test
    void shouldReturn422ForMoneyInvalid() throws Exception {
        mockMvc.perform(post("/test/money"))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.code").value("MONEY_INVALID"))
                .andExpect(jsonPath("$.detail").value("valor invalido"));
    }

    @Test
    void shouldReturn409ForIdempotencyConflict() throws Exception {
        mockMvc.perform(post("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT_IDEMPOTENCY"))
                .andExpect(jsonPath("$.detail").value("chave idempotency reaproveitada com payload diferente"));
    }

    @Test
    void shouldReturnInvalidBodyFields() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .header(IDEMPOTENCE_KEY, VALID_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"describe\": \" \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("um ou mais campos sao invalidos"))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("value", "describe")))
                .andExpect(jsonPath("$.errors[*].message",
                        containsInAnyOrder("valor obrigatorio", "descricao obrigatoria")));
    }

    @Test
    void shouldReturnBodyAndHeaderErrorsTogether() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .header(IDEMPOTENCE_KEY, "short")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 10.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("describe", IDEMPOTENCE_KEY)))
                .andExpect(jsonPath("$.errors[*].message",
                        containsInAnyOrder("descricao obrigatoria", "deve ter entre 16 e 64 caracteres")));
    }

    @Test
    void shouldReturnMissingHeader() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 10.00, \"describe\": \"ok\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value(IDEMPOTENCE_KEY))
                .andExpect(jsonPath("$.errors[0].message").value("header obrigatorio"));
    }
}
