package com.taesan.tikkle.domain.account.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

	private final AccountRepository accountRepository;

	public Integer getTotalQuantityByExchangeTypeAndPeriod(LocalDateTime startTime, LocalDateTime endTime,
		ExchangeType exchangeType) {
		Integer total = accountRepository.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime, exchangeType);
		log.info("총 total : {}", total);
		return total != null ? total : 0;
	}
}
