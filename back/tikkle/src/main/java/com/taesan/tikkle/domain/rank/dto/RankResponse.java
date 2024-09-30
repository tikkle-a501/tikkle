package com.taesan.tikkle.domain.rank.dto;

import java.util.List;

import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankResponse {

	private List<MemberRankResponse> rankList;
	private MemberRankResponse myRank;

	public static RankResponse of(List<MemberRankResponse> rankList, MemberRankResponse myRank){
		return RankResponse.builder()
			.rankList(rankList)
			.myRank(myRank)
			.build();
	}
}
