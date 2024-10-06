package com.taesan.tikkle.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllMemberBoardResponse {
    private List<MemberBoardResponse> postedPosts; // 게시중인 글
    private List<MemberBoardResponse> activePosts; // 진행중인 글
    private List<MemberBoardResponse> donePosts; // 완료된 글
}



