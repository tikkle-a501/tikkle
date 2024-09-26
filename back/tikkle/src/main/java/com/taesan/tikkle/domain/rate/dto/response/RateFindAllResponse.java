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
public class RateFindAllResponse {

	private UUID id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	private Integer timeToRank;

	@Builder
	private RateFindAllResponse(UUID id, LocalDateTime createdAt, Integer timeToRank) {
		this.id = id;
		this.createdAt = createdAt;
		this.timeToRank = timeToRank;
	}

	public static RateFindAllResponse from(Rate rate) {
		return RateFindAllResponse.builder()
			.id(rate.getId())
			.createdAt(rate.getCreatedAt())
			.timeToRank(rate.getTimeToRank())
			.build();
	}
}
