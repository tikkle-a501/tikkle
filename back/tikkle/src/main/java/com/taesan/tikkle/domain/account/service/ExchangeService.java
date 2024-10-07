package com.taesan.tikkle.domain.account.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.dto.request.ExchangeRequest;
import com.taesan.tikkle.domain.account.dto.response.ExchangeLogFindAllResponse;
import com.taesan.tikkle.domain.account.dto.response.ExchangeResponse;
import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import com.taesan.tikkle.domain.account.repository.ExchangeRepository;
import com.taesan.tikkle.domain.rate.entity.Rate;
import com.taesan.tikkle.domain.rate.service.RateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExchangeService {

	private final AccountService accountService;
	private final RateService rateService;
	private final ExchangeRepository exchangeRepository;

	public List<ExchangeLogFindAllResponse> getExchangeLogs(UUID memberId) {
		return accountService.findExchangeLogsByMemberId(memberId);
	}

	@Transactional
	public ExchangeResponse exchange(UUID memberId, ExchangeRequest exchangeRequest) {
		Rate rate = rateService.findByIdAndRecentRate(exchangeRequest.getRateId());
		Account account = accountService.updateAccount(memberId, exchangeRequest.getExchangeType(),
			rate.getTimeToRank(), exchangeRequest.getQuantity());
		exchangeRepository.save(
			ExchangeLog.of(rate, account, exchangeRequest.getExchangeType(), exchangeRequest.getQuantity()));
		if (exchangeRequest.getRateId().equals(rate.getId())) {
			return ExchangeResponse.from(false);
		}
		return ExchangeResponse.from(true);
	}
}