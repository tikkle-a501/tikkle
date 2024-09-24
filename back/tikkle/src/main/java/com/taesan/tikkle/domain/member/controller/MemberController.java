package com.taesan.tikkle.domain.member.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.member.dto.response.MemberResponse;
import com.taesan.tikkle.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

	private final MemberService memberService;

	MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<MemberResponse> getMemberResponse(@PathVariable(value = "id") UUID id) {
		try {
			MemberResponse memberResponse = memberService.getMember(id);
			return ResponseEntity.ok(memberResponse);
		} catch (IllegalStateException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
