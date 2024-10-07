package com.taesan.tikkle.domain.review.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {
	private UUID chatroomId;
	private String type;
}
