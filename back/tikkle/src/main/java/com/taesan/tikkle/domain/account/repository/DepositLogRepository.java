package com.taesan.tikkle.domain.account.repository;

import com.taesan.tikkle.domain.account.entity.DepositLog;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DepositLogRepository extends JpaRepository<DepositLog, UUID> {

    List<ExchangeLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
