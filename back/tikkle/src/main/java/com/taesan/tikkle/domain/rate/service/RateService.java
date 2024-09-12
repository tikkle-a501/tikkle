package com.taesan.tikkle.domain.rate.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.service.AccountService;
import com.taesan.tikkle.domain.rate.repository.RateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateService {

	private final AccountService accountService;
	private final RateRepository rateRepository;

	private final double initialCoinToStarRate = 100.0;  // 기본 환율 (1 시간 -> 100 랭킹포인트)

	@Transactional
	@Scheduled(fixedRate = 1000)  // 1시간마다 환율 갱신
	public void updateRate() {

	}
}
