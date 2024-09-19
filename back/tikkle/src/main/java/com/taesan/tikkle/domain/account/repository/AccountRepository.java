package com.taesan.tikkle.domain.account.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

	@Query("SELECT e FROM ExchangeLog e")
	List<ExchangeLog> findAllExchange();

	@Query("SELECT e FROM ExchangeLog e WHERE e.createdAt BETWEEN :startTime AND :endTime AND e.exchangeType = :exchangeType")
	List<ExchangeLog> findExchangeLogsBetweenAndByType(LocalDateTime startTime, LocalDateTime endTime,
		ExchangeType exchangeType);
}
