package com.taesan.tikkle.domain.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.dto.response.ExchangeLogFindAllResponse;
import com.taesan.tikkle.domain.account.entity.Account;
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
		Integer total = accountRepository.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime, exchangeType).orElse(0);
		log.info("총 total : {}", total);
		return total;
	}

	public List<ExchangeLogFindAllResponse> findExchangeLogsByMemberId(UUID memberId) {
		return accountRepository.findExchangeLogsByMemberId(memberId)
			.stream()
			.map(ExchangeLogFindAllResponse::from)
			.collect(Collectors.toList());
	}

	public Account updateAccount(UUID accountId, ExchangeType exchangeType, Integer rate, Integer quantity) {
		//TODO: 추후 예외 정의 하기
		Account account = accountRepository.findById(accountId)
			.orElseThrow(()-> new NoSuchElementException("계좌가 없습니다."));
		account.updateAccount(exchangeType, rate, quantity);
		return account;
	}
}
