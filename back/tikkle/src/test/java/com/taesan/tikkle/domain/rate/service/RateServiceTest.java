package com.taesan.tikkle.domain.rate.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.taesan.tikkle.domain.account.service.AccountService;
import com.taesan.tikkle.domain.rate.repository.RateRepository;

public class RateServiceTest {

	@InjectMocks
	private RateService rateService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void 포인트_수요증가_1시간당_포인트_환전량_감소() {
		// Given
		int totalTtoRAmount = 1000; // 포인트 환전에 사용된 시간
		int totalRtoTAmount = 500;  // 포인트로 바꾼 시간
		int previousRate = 1000;

		// When
		int newRate = rateService.calculateRate(totalTtoRAmount, totalRtoTAmount, previousRate);

		// Then
		System.out.println(newRate);
		assertTrue(newRate < previousRate, "포인트 환전량이 더 많으면 환율이 감소해야 한다.");
	}

	@Test
	public void 포인트_수요증가_1시간당_포인트_환전량_증가() {
		// Given
		int totalTtoRAmount = 500;  // 포인트 환전에 사용된 시간
		int totalRtoTAmount = 1000; // 포인트로 바꾼 시간
		int previousRate = 1000;

		// When
		int newRate = rateService.calculateRate(totalTtoRAmount, totalRtoTAmount, previousRate);

		// Then
		System.out.println(newRate);
		assertTrue(newRate > previousRate, "시간 생성량이 더 많으면 환율이 증가해야 한다.");
	}

	@Test
	public void 거래량이_같다면_이전환율_유지() {
		// Given
		int totalTtoRAmount = 100; // 포인트 환전에 사용된 시간
		int totalRtoTAmount = 100; // 포인트로 바꾼 시간
		int previousRate = 1000;

		// When
		int newRate = rateService.calculateRate(totalTtoRAmount, totalRtoTAmount, previousRate);

		// Then
		System.out.println(newRate);
		assertEquals(previousRate, newRate, "포인트 환전량과 시간 생성량이 같으면 환율이 변하지 않아야 한다.");
	}

	@Test
	public void 거래량이_없으면_이전환율_유지() {
		// Given
		int totalTtoRAmount = 0; // 포인트 환전에 사용된 시간
		int totalRtoTAmount = 0; // 포인트로 바꾼 시간
		int previousRate = 1000;

		// When
		int newRate = rateService.calculateRate(totalTtoRAmount, totalRtoTAmount, previousRate);

		// Then
		System.out.println(newRate);
		assertEquals(previousRate, newRate, "거래량이 없으면 이전 환율이 유지되어야 한다.");
	}

	@Test
	public void 거래량_비율이_같아도_거래량이_클수록_환율의_변동이_커져야한다_감소하는_경우() {
		// Given
		int firstTotalTtoRAmount = 10000; // 포인트 환전에 사용된 시간
		int firstTotalRtoTAmount = 1000;  // 포인트로 바꾼 시간
		int previousRate = 1000;

		int secondTotalTtoRAmount = 100; // 포인트 환전에 사용된 시간
		int secondTotalRtoTAmount = 10;  // 포인트로 바꾼 시간

		// When
		int firstNewRate = rateService.calculateRate(firstTotalTtoRAmount, firstTotalRtoTAmount, previousRate);
		int secondNewRate = rateService.calculateRate(secondTotalTtoRAmount, secondTotalRtoTAmount, previousRate);

		// Then
		assertTrue(previousRate - firstNewRate > previousRate - secondNewRate, "수요와 공급의 비가 같더라도 거래량이 클수록 더 환율에 가중치가 들어가야한다.");
	}

	@Test
	public void 거래량_비율이_같아도_거래량이_클수록_환율의_변동이_커져야한다_증가하는_경우() {
		// Given
		int firstTotalTtoRAmount = 1000; // 포인트 환전에 사용된 시간
		int firstTotalRtoTAmount = 10000;  // 포인트로 바꾼 시간
		int previousRate = 1000;

		int secondTotalTtoRAmount = 10; // 포인트 환전에 사용된 시간
		int secondTotalRtoTAmount = 100;  // 포인트로 바꾼 시간

		// When
		int firstNewRate = rateService.calculateRate(firstTotalTtoRAmount, firstTotalRtoTAmount, previousRate);
		int secondNewRate = rateService.calculateRate(secondTotalTtoRAmount, secondTotalRtoTAmount, previousRate);

		// Then
		assertTrue(firstNewRate - previousRate > secondNewRate - previousRate, "수요와 공급의 비가 같더라도 거래량이 클수록 더 환율에 가중치가 들어가야한다.");
	}
}
