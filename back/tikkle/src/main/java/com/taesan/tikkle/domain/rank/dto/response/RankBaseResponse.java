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
public class RankBaseResponse {

	public List<MemberRankResponse> rankList;

	public static RankBaseResponse from(List<MemberRankResponse> rankList){
		return RankBaseResponse.builder()
			.rankList(rankList)
			.build();
	}
}
