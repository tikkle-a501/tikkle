package com.taesan.tikkle.domain.account.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

	@Query("SELECT SUM(e.quantity) FROM ExchangeLog e WHERE e.createdAt BETWEEN :startTime AND :endTime AND e.exchangeType = :exchangeType")
	Integer getTotalQuantityByExchangeTypeAndPeriod(@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime,
		@Param("exchangeType") ExchangeType exchangeType);
}
