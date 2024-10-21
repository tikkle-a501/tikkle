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

	private static final int SIZE = 20;

	public List<MemberRankResponse> rankList;
	public int totalPages;
	public int currentPage;

	public static RankBaseResponse from(List<MemberRankResponse> rankList, int offset){
		return RankBaseResponse.builder()
			.rankList(rankList)
			.totalPages(rankList.size() / SIZE == 0 ? rankList.size() / SIZE : rankList.size() / SIZE + 1)
			.currentPage(offset)
			.build();
	}
}
