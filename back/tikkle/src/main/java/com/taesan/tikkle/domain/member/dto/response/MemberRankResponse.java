package com.taesan.tikkle.domain.member.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRankResponse {
	private UUID memberId;
	private String nickname;
	private long rankingPoint;
	private long tradeCount;
}
