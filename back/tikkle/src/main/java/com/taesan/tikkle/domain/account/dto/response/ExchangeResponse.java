package com.taesan.tikkle.domain.account.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExchangeResponse {
	private boolean rateChange;

	@Builder
	public ExchangeResponse(boolean rateChange) {
		this.rateChange = rateChange;
	}

	public static ExchangeResponse from(boolean rateChange) {
		return ExchangeResponse.builder()
			.rateChange(rateChange)
			.build();
	}
}
