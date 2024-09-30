package com.taesan.tikkle.domain.rate.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taesan.tikkle.domain.rate.entity.Rate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RateResponse {

	private UUID id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	private Integer timeToRank;

	@Builder
	private RateResponse(UUID id, LocalDateTime createdAt, Integer timeToRank) {
		this.id = id;
		this.createdAt = createdAt;
		this.timeToRank = timeToRank;
	}

	public static RateResponse from(Rate rate) {
		return RateResponse.builder()
			.id(rate.getId())
			.createdAt(rate.getCreatedAt())
			.timeToRank(rate.getTimeToRank())
			.build();
	}
}
