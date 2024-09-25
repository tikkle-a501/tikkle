package com.taesan.tikkle.domain.account.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExchangeLogFindAllResponse {

	private Integer time;
	private Integer rankingPoint;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	private ExchangeType exchangeType;

	@Builder
	public ExchangeLogFindAllResponse(Integer time, Integer rankingPoint, LocalDateTime createdAt,
		ExchangeType exchangeType) {
		this.time = time;
		this.rankingPoint = rankingPoint;
		this.createdAt = createdAt;
		this.exchangeType = exchangeType;
	}

	public static ExchangeLogFindAllResponse from(ExchangeLog exchangeLog) {
		return ExchangeLogFindAllResponse.builder()
			.time(exchangeLog.getQuantity())
			.rankingPoint(exchangeLog.getRate().getTimeToRank() * exchangeLog.getQuantity())
			.createdAt(exchangeLog.getCreatedAt())
			.exchangeType(exchangeLog.getExchangeType())
			.build();
	}
}
