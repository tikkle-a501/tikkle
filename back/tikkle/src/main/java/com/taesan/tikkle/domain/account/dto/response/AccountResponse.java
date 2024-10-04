package com.taesan.tikkle.domain.account.dto.response;

import java.util.UUID;

import com.taesan.tikkle.domain.account.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountResponse {

	public static AccountResponse from(Account account) {
		return new AccountResponse(
			account.getId(),
			account.getTimeQnt(),
			account.getRankingPoint()
		);
	}

	private UUID accountId;
	private Integer timeQnt;
	private Integer rankingPoint;
}

