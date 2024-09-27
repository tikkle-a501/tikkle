package com.taesan.tikkle.domain.rank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.rank.service.RankService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rank")
@RequiredArgsConstructor
public class RankController {
	private final RankService rankService;

	@GetMapping
	public ResponseEntity<?> getMemberRanking() {
		return ResponseEntity.ok(rankService.getRankList());
	}
}
