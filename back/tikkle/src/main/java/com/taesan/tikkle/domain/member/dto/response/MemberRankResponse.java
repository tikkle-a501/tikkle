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
	private long order;
	private String nickname;
	private long rankingPoint;
	private long tradeCount;

	public MemberRankResponse(UUID memberId, String nickname, long rankingPoint, long tradeCount) {
		this.memberId = memberId;
		this.nickname = nickname;
		this.rankingPoint = rankingPoint;
		this.tradeCount = tradeCount;
	}

	public void grantOrder(long order){
		this.order = order;
	}
}
