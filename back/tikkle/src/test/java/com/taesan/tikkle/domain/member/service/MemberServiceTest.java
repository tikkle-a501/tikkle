package com.taesan.tikkle.domain.member.service;

import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.member.dto.response.MemberResponse;
import com.taesan.tikkle.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	MemberService memberService;

	@Test
	public void shouldReturnValidMemberWhenValidId() {
		// Given
		UUID id = UlidCreator.getMonotonicUlid().toUuid();
		String name = "taesan";
		String nickname = "tikkle";
		String email = "taesan@tikkle.com";

		MemberResponse memberResponse =
			new MemberResponse(id, name, nickname, email);
		when(memberRepository.findById(id, MemberResponse.class))
			.thenReturn(Optional.of(memberResponse));

		// When
		MemberResponse response = memberService.getMember(id);

		// Then
		verify(memberRepository).findById(id, MemberResponse.class);

		Assertions.assertEquals(response.getId(), id);
		Assertions.assertEquals(response.getName(), name);
		Assertions.assertEquals(response.getNickname(), nickname);
		Assertions.assertEquals(response.getEmail(), email);
	}
}
