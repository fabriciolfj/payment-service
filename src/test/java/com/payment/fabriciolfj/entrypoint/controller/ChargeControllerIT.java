package com.payment.fabriciolfj.entrypoint.controller;

import com.payment.fabriciolfj.repository.charge.ChargeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static com.payment.fabriciolfj.constants.HeaderConstants.IDEMPOTENCE_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ChargeControllerIT {

    private static final String URL = "/api/v1/charge";

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private JsonMapper jsonMapper;

    @BeforeEach
    void clean() {
        jdbcClient.sql("delete from idempotency_key").update();
        chargeRepository.deleteAll();
    }

    private String create(final String key, final String body, final int expectedStatus) throws Exception {
        return mockMvc.perform(post(URL)
                        .header(IDEMPOTENCE_KEY, key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();
    }

    private String idOf(final String json) {
        return jsonMapper.readTree(json).get("id").asString();
    }

    @Test
    void shouldCreateChargeOnceAndReplayStoredResponse() throws Exception {
        final var key = "key-" + UUID.randomUUID();
        final var body = "{\"value\": 10.50, \"describe\": \"pedido 1\"}";

        final var first = create(key, body, 202);
        final var second = create(key, body, 202);

        assertThat(idOf(second)).isEqualTo(idOf(first));
        assertThat(chargeRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldReturnConflictWhenKeyReusedWithAnotherPayload() throws Exception {
        final var key = "key-" + UUID.randomUUID();

        create(key, "{\"value\": 10.50, \"describe\": \"pedido 1\"}", 202);

        mockMvc.perform(post(URL)
                        .header(IDEMPOTENCE_KEY, key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\": 99.90, \"describe\": \"pedido 1\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT_IDEMPOTENCY"));

        assertThat(chargeRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldFindCreatedCharge() throws Exception {
        final var id = idOf(create("key-" + UUID.randomUUID(), "{\"value\": 10.50, \"describe\": \"pedido 1\"}", 202));

        mockMvc.perform(get(URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.value").value(10.50))
                .andExpect(jsonPath("$.describe").value("pedido 1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturnNotFoundForUnknownCharge() throws Exception {
        mockMvc.perform(get(URL + "/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CHARGE_NOT_FOUND"))
                .andExpect(jsonPath("$.detail").value("cobranca nao encontrada"));
    }
}
