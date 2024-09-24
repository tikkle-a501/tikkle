package com.taesan.tikkle.domain.rate.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import com.taesan.tikkle.domain.account.service.AccountService;
import com.taesan.tikkle.domain.rate.repository.RateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {

	private final AccountService accountService;
	private final RateRepository rateRepository;

	private final double initialCoinToStarRate = 100.0;

	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void updateRate() {
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime oneHourAgo = now.minusHours(1);
		log.info("{}, {}", now, oneHourAgo);

//		List<ExchangeLog> logs = accountService.findExchangeLogsBetween(oneHourAgo.withMinute(0).withSecond(0),
//			oneHourAgo.withMinute(59).withSecond(59), );

//		logs.forEach(log -> {
//		});
	}
}
