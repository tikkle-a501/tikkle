package com.taesan.tikkle.domain.member.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;
import com.taesan.tikkle.domain.member.dto.response.MemberResponse;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	/*
		TODO: 에러 코드 정리 필요
	 */
	public Member getMember(UUID id) {
		Member member = memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

		return member;
	}

	public MemberResponse getMemberResponse(UUID id) {
		Member member =
			memberRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
		return MemberResponse.from(member);
	}

	public List<MemberRankResponse> findMemberRankings() {
		return memberRepository.findMemberRankings();
	}
}
