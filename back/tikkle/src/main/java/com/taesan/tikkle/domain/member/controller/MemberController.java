package com.taesan.tikkle.domain.member.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.member.dto.response.MemberResponse;
import com.taesan.tikkle.domain.member.service.MemberService;
import com.taesan.tikkle.global.annotations.AuthedUsername;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	private final MemberService memberService;

	@GetMapping("")
	public ResponseEntity<MemberResponse> getMemberResponse(@AuthedUsername UUID username) {
		try {
			logger.debug("memberId: {}", username.toString());
			MemberResponse memberResponse = memberService.getMemberResponse(username);
			logger.debug("memberResponse: {}", memberResponse);
			return ResponseEntity.ok(memberResponse);
		} catch (IllegalStateException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
