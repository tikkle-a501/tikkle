package com.taesan.tikkle.domain.member.dto.response;

public interface MemberRankProjection {

	String getMemberId();
	String getNickname();
	long getRankingPoint();
	long getTradeCount();
}
