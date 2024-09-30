package com.taesan.tikkle.domain.rate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.rate.dto.response.RateResponse;
import com.taesan.tikkle.domain.rate.service.RateService;
import com.taesan.tikkle.global.response.ApiResponse;

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
	public ResponseEntity<ApiResponse<List<RateResponse>>> findAll() {
		ApiResponse<List<RateResponse>> response = ApiResponse.success("환율 조회를 성공했습니다.", rateService.findAll());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/recent")
	public ResponseEntity<ApiResponse<RateResponse>> findTopByOrderByCreatedAtDesc() {
		ApiResponse<RateResponse> response = ApiResponse.success("최근 환율 조회를 성공했습니다.",
			rateService.findTopByOrderByCreatedAtDesc());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
