package com.taesan.tikkle.domain.review.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.review.dto.request.CreateReviewRequest;
import com.taesan.tikkle.domain.review.dto.response.ReviewListResponse;
import com.taesan.tikkle.domain.review.service.ReviewService;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import com.taesan.tikkle.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("")
	public ResponseEntity<ApiResponse<UUID>> createReview(@RequestBody CreateReviewRequest request,
		@AuthedUsername UUID memberId) {
		return ResponseEntity.ok(
			ApiResponse.success("리뷰가 성공적으로 생성되었습니다.", reviewService.createReview(request, memberId)));
	}

	// TODO : Get Review
	@GetMapping("")
	public ResponseEntity<ApiResponse<List<ReviewListResponse>>> getReviews(@AuthedUsername UUID memberId) {
		ApiResponse<List<ReviewListResponse>> response = ApiResponse.success("리뷰 조회를 성공하였습니다.",reviewService.getReviews(memberId));
		return ResponseEntity.ok(response);
	}
}
