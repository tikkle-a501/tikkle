package com.taesan.tikkle.domain.review.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.review.dto.request.CreateReviewRequest;
import com.taesan.tikkle.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("")
	public ResponseEntity<UUID> createReview(@RequestBody CreateReviewRequest request) {
		return ResponseEntity.ok(reviewService.createReview(request));
	}

}
