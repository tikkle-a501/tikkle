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
	private final static double RATE_ADJUSTMENT_FACTOR = 1.0;

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

		//거래량이 모두 0인 경우 이전 환율 유지
		if (totalTransactionVolume == 0) {
			return previousRate;
		}

		// 변동폭 완화 상수 적용
		double volatilityReductionFactor = 0.1; // 변동폭 완화를 위해 10%로 조정 (alpha)

		// 환율 변동폭 가중치: 거래량이 많을수록 변동폭이 커짐 (로그 함수 사용하여 급격한 변화 방지)
		double transactionWeightFactor = 1 + Math.log(1 + totalTransactionVolume);

		// 비율 계산: TtoR / RtoT 비율에 따라 변동폭 결정
		double rateChangeFactor;

		if (totalTtoRAmount > totalRtoTAmount) {
			// TtoR > RtoT: 환율 상승
			rateChangeFactor = (double)totalTtoRAmount / totalRtoTAmount;
		} else if (totalTtoRAmount < totalRtoTAmount) {
			// TtoR < RtoT: 환율 하락
			rateChangeFactor = (double)totalRtoTAmount / totalTtoRAmount;
		} else {
			// TtoR == RtoT: 변동폭이 작음
			rateChangeFactor = 1;
		}

		// 최종 변동폭 계산: 이전 환율 * (1 + 변동폭 * 가중치 * 완화 상수)
		double rateChange = (rateChangeFactor - 1) * transactionWeightFactor * volatilityReductionFactor;

		// 새로운 환율을 계산 (이전 환율에 변동폭을 더함)
		int newRate = (int)(previousRate * (1 + rateChange));

		return newRate;
	}
}
