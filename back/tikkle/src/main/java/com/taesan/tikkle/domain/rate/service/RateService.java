package com.taesan.tikkle.domain.rate.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.service.AccountService;
import com.taesan.tikkle.domain.rate.dto.response.RateResponse;
import com.taesan.tikkle.domain.rate.entity.Rate;
import com.taesan.tikkle.domain.rate.repository.RateRepository;
import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

	private final static int INITIAL_TIME_TO_RANK_POINT = 1000;
	private final static double RATE_ADJUSTMENT_FACTOR = 0.03;

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

	public List<RateResponse> findAll() {
		return findAllResponses(true);
	}

	public List<RateResponse> findAllByOrderByCreatedAtAsc() {
		return findAllResponses(false);
	}

	public RateResponse findTopByOrderByCreatedAtDesc() {
		Rate rate = rateRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new CustomException(ErrorCode.RATE_NOT_EXIST));
		return RateResponse.from(rate);
	}

	public Rate findByIdAndRecentRate(UUID rateId) {
		Rate rate = rateRepository.findById(rateId).orElseThrow(() -> new CustomException(ErrorCode.RATE_NOT_FOUND));
		Rate recentRate = rateRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new CustomException(ErrorCode.RATE_NOT_FOUND));
		if (rate.getId().equals(recentRate.getId())) {
			return rate;
		}
		return recentRate;
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

	public int calculateRate(int totalTtoRAmount, int totalRtoTAmount, int previousRate) {
		int totalTransactionVolume = totalTtoRAmount + totalRtoTAmount;

		//거래량이 0이였다면 이전 환율 적용
		if (totalTransactionVolume == 0) {
			return previousRate;
		}

		//직전 환율의 10%만 변화
		double rateChangeLimit = previousRate * RATE_ADJUSTMENT_FACTOR;
		int newRate;
		int rateAdjustment;

		//포인트 환전에 사용된 시간이 더 많다 -> 포인트의 수요 증가 -> 1시간 당 바꿀 수 있는 포인트 감소
		if (totalTtoRAmount > totalRtoTAmount) {
			//거래량 비율 계산 -> 환전에 사용된 시간이 분모
			rateAdjustment = getWeight(totalRtoTAmount, totalTtoRAmount, rateChangeLimit);
			newRate = previousRate - rateAdjustment;
		} else if (totalRtoTAmount > totalTtoRAmount) {
			//거래량 비율 계산 -> 환전으로 생성된 시간이 분모
			rateAdjustment = getWeight(totalTtoRAmount, totalRtoTAmount, rateChangeLimit);
			newRate = previousRate + rateAdjustment;
		} else {
			newRate = previousRate;
		}

		log.info("시간 -> 포인트: {}, 포인트 -> 시간: {}, 새로운 환율: {}", totalTtoRAmount, totalRtoTAmount, newRate);
		return newRate;
	}

	private int getWeight(int numerator, int denominator, double rateChangeLimit) {
		//거래량 비율 -> 분자와 분모의 차이가 커질 수록 한 쪽으로의 수요가 커지기 때문에 1을 빼서 반전
		double demandDiscrepancyRatio = 1 - ((double)numerator / (double)denominator);
		//거래량에 따른 가중치 값
		double transactionVolumeImpact = Math.log(numerator + denominator);

		return (int)Math.round(rateChangeLimit * demandDiscrepancyRatio + transactionVolumeImpact);
	}

	private List<RateResponse> findAllResponses(boolean sortByCreatedAtAsc) {
		Stream<RateResponse> rateStream = rateRepository.findAll().stream()
			.map(RateResponse::from);
		if (sortByCreatedAtAsc) {
			rateStream = rateStream.sorted(Comparator.comparing(RateResponse::getCreatedAt));
		}
		return rateStream.collect(Collectors.toList());
	}
}
