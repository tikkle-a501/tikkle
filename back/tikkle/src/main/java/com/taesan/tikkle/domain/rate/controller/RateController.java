package com.taesan.tikkle.domain.rate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.rate.dto.response.RateFindAllResponse;
import com.taesan.tikkle.domain.rate.service.RateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rate")
@RequiredArgsConstructor
public class RateController {
	private final RateService rateService;

	@PostMapping
	public void updateRate() {
		rateService.updateRate();
	}

	@GetMapping
	public ResponseEntity<List<RateFindAllResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(rateService.findAll());
	}
}
