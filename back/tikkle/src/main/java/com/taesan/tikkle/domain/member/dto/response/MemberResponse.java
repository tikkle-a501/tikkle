package com.taesan.tikkle.domain.member.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
	private UUID id;
	private String name;
	private String nickname;
	private String provider;
	private String email;
}
