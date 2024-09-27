package com.taesan.tikkle.domain.member.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;
import com.taesan.tikkle.domain.member.dto.response.MemberResponse;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;

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
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return member;
	}

	public MemberResponse getMemberResponse(UUID id) {
		Member member =
			memberRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		return MemberResponse.from(member);
	}

	public List<MemberRankResponse> findMemberRankings() {
		return memberRepository.findMemberRankings();
	}
}
