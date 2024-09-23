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

	private final static int INITIAL_TIME_TO_RANK_POINT = 1000;
	private final static double RATE_ADJUSTMENT_FACTOR = 0.1;

	private final AccountService accountService;
	private final RateRepository rateRepository;

	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void updateRate() {

		LocalDateTime oneHourAgo = getOneHourAgo();

		LocalDateTime startTime = oneHourAgo.withMinute(0).withSecond(0);
		LocalDateTime endTime = oneHourAgo.withMinute(59).withSecond(59);

		int previousRate = getPreviousRate();
		log.info("이전 환율: {}", previousRate);

		int totalTtoRAmount = accountService.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime,
			ExchangeType.TTOR);
		int totalRtoTAmount = accountService.getTotalQuantityByExchangeTypeAndPeriod(startTime, endTime,
			ExchangeType.RTOT);

		int newRate = calculateRate(totalTtoRAmount, totalRtoTAmount, previousRate);

		rateRepository.save(Rate.builder().timeToRank(newRate).build());
	}

	private LocalDateTime getOneHourAgo() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneHourAgo = now.minusHours(1);
		log.info("현재 시간: {}, 한 시간 전: {}", now, oneHourAgo);
		return oneHourAgo;
	}

	private int getPreviousRate() {
		return rateRepository.findTopByOrderByCreatedAtDesc()
			.map(Rate::getTimeToRank)
			.orElse(INITIAL_TIME_TO_RANK_POINT);
	}

	// 환율 계산 로직
	public int calculateRate(int totalTtoRAmount, int totalRtoTAmount, int previousRate) {
		int totalTransactionVolume = totalTtoRAmount + totalRtoTAmount;

		if (totalTransactionVolume == 0) {
			return previousRate;
		}

		double maxRateChange = previousRate * RATE_ADJUSTMENT_FACTOR;
		int rateChangePercentage;
		int newRate;

		if (totalTtoRAmount > totalRtoTAmount) {
			//거래량 비율계산
			double supplyDemandRatio = (double)totalRtoTAmount / (double)totalTtoRAmount;
			//거래량 불균형 계산 -> 1에 가까울수록 수요와 공급이 균형을 이룸 (0~1 사이 값)
			//최대 변동 값 사이에서만
			rateChangePercentage = (int)Math.round((1 - supplyDemandRatio) * maxRateChange);
			newRate = previousRate - rateChangePercentage;
		} else if (totalRtoTAmount > totalTtoRAmount) {
			double demandSupplyRatio = (double)totalTtoRAmount / (double)totalRtoTAmount;
			rateChangePercentage = (int)Math.round((1 - demandSupplyRatio) * maxRateChange);
			newRate = previousRate + rateChangePercentage;
		} else {
			newRate = previousRate;
		}
		return newRate;
	}
}
