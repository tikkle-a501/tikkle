package com.taesan.tikkle.domain.rate.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.service.AccountService;
import com.taesan.tikkle.domain.rate.entity.Rate;
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
		Long totalTime = accountService.sumTotalTime();
		Long totalRankingPoint = accountService.sumTotalRankingPoint();

		double coinToStarRate;

		if (totalTime == null || totalRankingPoint == null || totalTime == 0 || totalRankingPoint == 0) {
			coinToStarRate = initialCoinToStarRate;  // 기본 환율 사용
		} else {
			// 총 시간과 랭킹포인트 비율 계산 후 올림 처리하여 정수 환율로 변환
			coinToStarRate = Math.ceil((double)totalRankingPoint / totalTime);
		}

		// 새로운 환율 저장
		rateRepository.save(Rate.builder().timeToRank((int)coinToStarRate).build());

		System.out.println("새로운 환율 적용: 1시간 -> " + (int)coinToStarRate + " 포인트");
	}
}
