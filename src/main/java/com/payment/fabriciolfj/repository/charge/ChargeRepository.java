package com.payment.fabriciolfj.repository.charge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChargeRepository extends JpaRepository<ChargeEntity, UUID> {
}
