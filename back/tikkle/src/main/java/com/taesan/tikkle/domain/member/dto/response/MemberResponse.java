package com.taesan.tikkle.domain.member.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberResponse {
	private UUID id;
	private String name;
	private String nickname;
	private String email;
}
