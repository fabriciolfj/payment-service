package com.payment.fabriciolfj.repository.idempotency;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IdempotencyRepository.class)
class IdempotencyRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private IdempotencyRepository repository;

    private static IdempotencyKeyEntity entity(final String hash, final String response) {
        return IdempotencyKeyEntity.builder()
                .key("0123456789abcdef")
                .requestHash(hash)
                .statusHttp(201)
                .response(response)
                .build();
    }

    @Test
    void shouldReturnEmptyWhenInserted() {
        assertThat(repository.persist(entity("hash-A", "{\"id\":\"1\"}"))).isEmpty();
    }

    @Test
    void shouldReturnStoredIdempotencyOnConflict() {
        repository.persist(entity("hash-A", "{\"id\":\"1\"}"));

        final var existing = repository.persist(entity("hash-B", "{\"id\":\"2\"}"));

        assertThat(existing).hasValueSatisfying(stored -> {
            assertThat(stored.getKey()).isEqualTo("0123456789abcdef");
            assertThat(stored.getRequestHash()).isEqualTo("hash-A");
            assertThat(stored.getStatusHttp()).isEqualTo(201);
            assertThat(stored.getResponse()).isEqualTo("{\"id\": \"1\"}");
            assertThat(stored.getCreatedOn()).isNotNull();
        });
    }
}
