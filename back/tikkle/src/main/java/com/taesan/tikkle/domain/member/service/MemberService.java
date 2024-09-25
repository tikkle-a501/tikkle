package com.taesan.tikkle.domain.member.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.member.dto.response.MemberResponse;
import com.taesan.tikkle.domain.member.repository.MemberRepository;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	/*
		FIXME: 추후 id를 직접 받지 않고 Spring Security 이용하는 방식으로 발전 필요
		TODO: 에러 코드 정리 필요
	 */
	public MemberResponse getMember(UUID id) {
		MemberResponse member =
			memberRepository.findById(id, MemberResponse.class)
				.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		return member;
	}

}
