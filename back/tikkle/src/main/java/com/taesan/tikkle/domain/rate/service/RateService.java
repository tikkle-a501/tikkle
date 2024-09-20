package com.taesan.tikkle.domain.rate.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.service.AccountService;
import com.taesan.tikkle.domain.rate.entity.Rate;
import com.taesan.tikkle.domain.rate.repository.RateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

	private final AccountService accountService;
	private final RateRepository rateRepository;

	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void updateRate() {

		LocalDateTime oneHourAgo = getOneHourAgo();

		LocalDateTime startTime = oneHourAgo.withMinute(0).withSecond(0);
		LocalDateTime endTime = oneHourAgo.withMinute(59).withSecond(59);

		int totalTtoRAmount = accountService.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime,
			ExchangeType.TTOR);
		int totalRtoTAmount = accountService.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime,
			ExchangeType.RTOT);

		int newRate = calculateRate(totalTtoRAmount, totalRtoTAmount);

		rateRepository.save(Rate.builder().timeToRank(newRate).build());
	}

	private LocalDateTime getOneHourAgo() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneHourAgo = now.minusHours(1);
		log.info("현재 시간: {}, 한 시간 전: {}", now, oneHourAgo);
		return oneHourAgo;
	}

	// 환율 계산 로직
	private int calculateRate(int totalTtoRAmount, int totalRtoTAmount) {
		// 기본적으로 totalTtoRAmount (시간 -> 포인트)와 totalRtoTAmount (포인트 -> 시간)의 비율을 기준으로 환율을 조정
		int initialCoinToStarRate = 1000;
		return initialCoinToStarRate;
	}
}
