package com.taesan.tikkle.domain.account.dto.request;

import java.util.UUID;

import com.taesan.tikkle.domain.account.dto.ExchangeType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExchangeRequest {
	private UUID rateId;
	private Integer timeToRank;
	private Integer quantity;
	private ExchangeType exchangeType;
}
