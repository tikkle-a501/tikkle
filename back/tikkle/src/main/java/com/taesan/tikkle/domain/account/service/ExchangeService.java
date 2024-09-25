package com.taesan.tikkle.domain.account.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.dto.response.ExchangeLogFindAllResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExchangeService {

	private final AccountService accountService;

	public List<ExchangeLogFindAllResponse> getExchangeLogs(UUID memberId) {
		return accountService.findExchangeLogsByMemberId(memberId);
	}
}
