package com.taesan.tikkle.domain.config.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

public class CustomUserDetails implements UserDetails {

	// UUID를 직접 반환하는 메서드 (필요에 따라 추가)
	@Getter
	private final UUID memberId;  // UUID 값
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(UUID memberId, Collection<? extends GrantedAuthority> authorities) {
		this.memberId = memberId;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;  // OAuth2 인증에서는 비밀번호를 사용하지 않음
	}

	@Override
	public String getUsername() {
		// UUID를 반환하여 username 대신 사용
		return memberId.toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

