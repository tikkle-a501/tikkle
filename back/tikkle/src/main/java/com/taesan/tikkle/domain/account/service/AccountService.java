package com.taesan.tikkle.domain.account.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	public Integer getTotalQuantityByExchangeTypeAndPeriod(LocalDateTime startTime, LocalDateTime endTime,
		ExchangeType exchangeType) {
		Integer total = accountRepository.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime, exchangeType);
		return total != null ? total : 0;
	}
}
