package com.payment.fabriciolfj.repository.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IdempotencyRepository {


    private static final String SQL_INSERT = """
            insert into idempotency_key (key, request_hash, status_http, response)
            values (:key, :request_hash, :status_http, cast(:response as jsonb))
            on conflict (key) do update set key = excluded.key
            returning key, request_hash, status_http, response::text as response, created_on, (xmax = 0) as inserted
            """;

    private final JdbcClient jdbcClient;


    public Optional<IdempotencyKeyEntity> persist(final IdempotencyKeyEntity idempotencyKeyEntity) {
        return jdbcClient.sql(SQL_INSERT)
                .param("key", idempotencyKeyEntity.getKey())
                .param("request_hash", idempotencyKeyEntity.getRequestHash())
                .param("status_http", idempotencyKeyEntity.getStatusHttp())
                .param("response", idempotencyKeyEntity.getResponse())
                .query(IdempotencyPersistResultRowMapper.INSTANCE)
                .single()
                .existing();
    }
}
