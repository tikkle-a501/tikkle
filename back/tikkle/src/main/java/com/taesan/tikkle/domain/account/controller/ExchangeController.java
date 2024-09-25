package com.taesan.tikkle.domain.account.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.f4b6a3.ulid.UlidCreator;
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
		UUID memberId = UlidCreator.getMonotonicUlid().toUuid();
		return ResponseEntity.status(HttpStatus.OK)
			.body(exchangeService.getExchangeLogs(UUID.fromString("01920dd1-423e-f86b-a4dd-28a20d81fab0")));
	}
}
