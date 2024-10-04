package com.taesan.tikkle.domain.member.dto.response;

import java.util.UUID;

import com.taesan.tikkle.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberBriefResponse {

	public static MemberBriefResponse from(Member member) {
		return new MemberBriefResponse(
			member.getId(),
			member.getName()
		);
	}

	private UUID id;
	private String name;
}

