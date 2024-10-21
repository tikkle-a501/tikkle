package com.taesan.tikkle.domain.rank.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.member.dto.response.MemberRankProjection;
import com.taesan.tikkle.domain.rank.dto.response.RankBaseResponse;
import com.taesan.tikkle.domain.rank.service.RankService;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import com.taesan.tikkle.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RankController {
	private final RankService rankService;

	//1. jpql 조인 전체 데이터 + 레디스 sorted set
	@GetMapping("/v1/rank")
	public ResponseEntity<ApiResponse<RankBaseResponse>> getMemberRanking(@AuthedUsername UUID username) {
		ApiResponse<RankBaseResponse> response = ApiResponse.success("랭킹 조회에 성공했습니다.",
			rankService.getRankList(username));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	//2. nativeQuery를 활용해 limit, order by 활용해 데이터 조회
	@GetMapping("/v2/rank")
	public ResponseEntity<ApiResponse<List<MemberRankProjection>>> getMemberRanking(@AuthedUsername UUID username, @RequestParam(required = false, defaultValue = "20") int limit, @RequestParam(required = false, defaultValue = "0") int offset) {
		ApiResponse<List<MemberRankProjection>> response = ApiResponse.success("랭킹 조회에 성공했습니다.",
			rankService.getRankList(limit, offset));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("search")
	public ResponseEntity<ApiResponse<RankBaseResponse>> getSearchRanking(@RequestParam String keyword) {
		ApiResponse<RankBaseResponse> response = ApiResponse.success("랭킹 조회에 성공했습니다",
			rankService.getSearchRankList(keyword));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
