package com.taesan.tikkle.domain.rank.dto.response;

import java.util.List;

import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RankResponse extends RankBaseResponse{

	private MemberRankResponse myRank;

	public static RankResponse of(List<MemberRankResponse> rankList, MemberRankResponse myRank) {
		return RankResponse.builder()
			.rankList(rankList)
			.myRank(myRank)
			.build();
	}
}
