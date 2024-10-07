package com.taesan.tikkle.domain.account.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.account.dto.request.ExchangeRequest;
import com.taesan.tikkle.domain.account.dto.response.ExchangeLogFindAllResponse;
import com.taesan.tikkle.domain.account.service.ExchangeService;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import com.taesan.tikkle.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

	private final ExchangeService exchangeService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<ExchangeLogFindAllResponse>>> getExchangeLogs(
		@AuthedUsername UUID username) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success("환전 내역 조회를 성공했습니다", exchangeService.getExchangeLogs(username)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<?>> exchange(@AuthedUsername UUID username,
		@RequestBody ExchangeRequest exchangeRequest) {
		exchangeService.exchange(username, exchangeRequest);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("환전을 성공했습니다.", null));
	}
}
