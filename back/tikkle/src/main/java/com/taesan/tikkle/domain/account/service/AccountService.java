package com.taesan.tikkle.domain.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.dto.response.ExchangeLogFindAllResponse;
import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.repository.AccountRepository;
import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

	private final AccountRepository accountRepository;

	public Integer getTotalQuantityByExchangeTypeAndPeriod(LocalDateTime startTime, LocalDateTime endTime,
		ExchangeType exchangeType) {
		Integer total = accountRepository.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime, exchangeType)
			.orElse(0);
		log.info("총 total : {}", total);
		return total;
	}

	public List<ExchangeLogFindAllResponse> findExchangeLogsByMemberId(UUID memberId) {
		return accountRepository.findExchangeLogsByMemberId(memberId)
			.stream()
			.map(ExchangeLogFindAllResponse::from)
			.collect(Collectors.toList());
	}

	public Account updateAccount(UUID memberId, ExchangeType exchangeType, Integer rate, Integer quantity) {
		Account account = accountRepository.findByMember_Id(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
		account.updateAccount(exchangeType, rate, quantity);
		return account;
	}

	public Account fetchAccount(UUID memberId) {
		Account account = accountRepository.findByMember_Id(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

		return account;
	}
}
