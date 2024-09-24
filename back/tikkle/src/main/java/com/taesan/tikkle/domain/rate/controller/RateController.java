package com.taesan.tikkle.domain.rate.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
