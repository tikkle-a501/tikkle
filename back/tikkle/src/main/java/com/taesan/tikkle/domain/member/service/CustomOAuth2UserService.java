package com.taesan.tikkle.domain.member.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import com.taesan.tikkle.domain.organization.entity.Organization;
import com.taesan.tikkle.domain.organization.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
	private final MemberRepository memberRepository;
	private final OrganizationRepository organizationRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 기본 OAuth2UserService 사용하여 사용자 정보를 가져옴
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		logger.debug("CustomOAuth2UserService 도달");

		// Mattermost로부터 가져온 사용자 정보
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("username"); // OAuth 프로필의 이름
		String nickname = oAuth2User.getAttribute("nickname"); // 필요에 따라 가져올 수 있음

		// 사용자를 조회하거나 생성
		Member member = saveOrUpdateMember(email, name, nickname);

		Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
		attributes.put("memberId", member.getId());
		// OAuth2User 객체를 반환
		return new DefaultOAuth2User(
			Collections.singleton(new OAuth2UserAuthority(oAuth2User.getAttributes())),
			attributes,
			"memberId" // 기본 사용자 이름 속성
		);
	}

	private Member saveOrUpdateMember(String email, String name, String nickname) {
		// 이미 존재하는지 확인
		Optional<Member> existingMemberOpt = memberRepository.findByEmail(email);

		// 존재하는 경우 업데이트
		if (existingMemberOpt.isPresent()) {
			Member existingMember = existingMemberOpt.get();
			existingMember = updateExistingMember(existingMember, name, nickname);
			return memberRepository.save(existingMember);
		} else {
			// 존재하지 않으면 새로 생성
			Organization defaultOrganization = getDefaultOrganization();
			Member newMember = Member.builder()
				.organization(defaultOrganization)
				.name(name)
				.nickname(nickname)
				.email(email)
				.build();
			return memberRepository.save(newMember);
		}
	}

	// 기존 Member를 업데이트하는 메서드
	private Member updateExistingMember(Member existingMember, String name, String nickname) {
		// 필요한 정보 업데이트
		existingMember.changeName(name);
		existingMember.changeNickname(nickname);
		return existingMember;
	}

	/*
		TODO: 내부 Mattermost 서버 이용 코드이므로, 추후 여러 서버 기반으로 변경 필요
	 */
	private Organization getDefaultOrganization() {
		return organizationRepository.findByName("DEFAULT").orElse(organizationRepository.save(
			new Organization("FREE",
				"j11a501.p.ssafy.io",
				1,
				null,
				"DEFAULT")));
	}
}
