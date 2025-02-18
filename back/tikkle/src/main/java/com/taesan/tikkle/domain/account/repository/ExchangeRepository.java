package com.taesan.tikkle.domain.account.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.account.entity.ExchangeLog;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeLog, UUID> {

    List<ExchangeLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
