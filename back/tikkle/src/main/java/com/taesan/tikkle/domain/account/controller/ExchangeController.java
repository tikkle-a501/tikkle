package com.taesan.tikkle.domain.account.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.account.dto.request.ExchangeRequest;
import com.taesan.tikkle.domain.account.dto.response.ExchangeLogFindAllResponse;
import com.taesan.tikkle.domain.account.service.ExchangeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

	private final ExchangeService exchangeService;

	@GetMapping
	public ResponseEntity<List<ExchangeLogFindAllResponse>> getExchangeLogs(Authentication authentication) {
		//TODO: 추후 사용자 인증 처리 로직 결정 되면 수정해야합니다.
		UUID memberId = UUID.fromString("01920dd1-423e-f86b-a4dd-28a20d81fab0");
		return ResponseEntity.status(HttpStatus.OK)
			.body(exchangeService.getExchangeLogs(memberId));
	}

	@PostMapping
	public ResponseEntity<?> exchange(@RequestBody ExchangeRequest exchangeRequest) {
		//TODO: 추후 사용자 인증 처리 로직 결정 되면 수정해야합니다.
		UUID memberId = UUID.fromString("01920dd1-423e-f86b-a4dd-28a20d81fab0");
		exchangeService.exchange(memberId, exchangeRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
