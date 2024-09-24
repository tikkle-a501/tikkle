package com.taesan.tikkle.domain.rate.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taesan.tikkle.domain.rate.entity.Rate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RateFindAllResponse {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	private Integer timeToRank;

	@Builder
	public RateFindAllResponse(LocalDateTime createdAt, Integer timeToRank) {
		this.createdAt = createdAt;
		this.timeToRank = timeToRank;
	}

	public static RateFindAllResponse from(Rate rate) {
		return RateFindAllResponse.builder()
			.createdAt(rate.getCreatedAt())
			.timeToRank(rate.getTimeToRank())
			.build();
	}
}
