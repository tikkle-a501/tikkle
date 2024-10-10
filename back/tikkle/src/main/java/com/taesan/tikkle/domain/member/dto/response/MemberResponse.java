package com.taesan.tikkle.domain.member.dto.response;

import java.util.UUID;

import com.taesan.tikkle.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberResponse {

	/*
		NOTE: image가 크게 필요 없는 경우 이용
	 */
	public static MemberResponse from(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getName(),
			member.getNickname(),
			member.getEmail(),
			null
		);
	}

	public static MemberResponse from(Member member, byte[] image) {
		return new MemberResponse(
			member.getId(),
			member.getName(),
			member.getNickname(),
			member.getEmail(),
			image
		);
	}

	private UUID id;
	private String name;
	private String nickname;
	private String email;
	private byte[] image;
}
