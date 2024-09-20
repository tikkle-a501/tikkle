package com.taesan.tikkle.domain.account.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import com.taesan.tikkle.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	public List<ExchangeLog> findExchangeLogsBetween(LocalDateTime startTime, LocalDateTime endTime,
		ExchangeType exchangeType) {
		return accountRepository.findExchangeLogsBetweenAndByType(startTime, endTime, exchangeType);
	}
}
