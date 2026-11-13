package com.payment.fabriciolfj.repository.idempotency;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

final class IdempotencyPersistResultRowMapper implements RowMapper<IdempotencyPersistResult> {

    static final IdempotencyPersistResultRowMapper INSTANCE = new IdempotencyPersistResultRowMapper();

    private IdempotencyPersistResultRowMapper() { }

    @Override
    public IdempotencyPersistResult mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final var entity = IdempotencyKeyEntity.builder()
                .key(rs.getString("key"))
                .requestHash(rs.getString("request_hash"))
                .statusHttp(rs.getObject("status_http", Integer.class))
                .response(rs.getString("response"))
                .createdOn(rs.getObject("created_on", OffsetDateTime.class).toInstant())
                .build();

        return new IdempotencyPersistResult(entity, rs.getBoolean("inserted"));
    }
}
