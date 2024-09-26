package com.taesan.tikkle.domain.config.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taesan.tikkle.domain.member.service.CustomOAuth2UserService;
import com.taesan.tikkle.domain.member.service.CustomUserDetailsService;
import com.taesan.tikkle.global.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomUserDetailsService customUserDetailsService;
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		logger.debug("cookies: {}", request.getCookies());

		String jwtToken = null;

		// 쿠키에서 "Authorization" 쿠키를 찾음
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("Authorization".equals(cookie.getName())) {
					jwtToken = cookie.getValue();
					break;
				}
			}
		}

		if (jwtToken == null) {
			filterChain.doFilter(request, response);
			logger.debug("jwt token null!");
			return;
		}

		String username = null;
		String accessToken = null;

		if (!jwtToken.startsWith("Bearer+")) {
			logger.debug("Invalid auth header: {}", jwtToken);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
			return;
		}

		accessToken = jwtToken.substring(7);
		logger.debug("accessToken: {}", accessToken);
		try {
			username = jwtUtil.extractUsername(accessToken);
		} catch (Exception e) {
			logger.error("JWT 추출 실패: " + e.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
			return;
		}

		if (username == null) {
			throw new IllegalArgumentException("No username found in the token.");
		}

		CustomUserDetails userDetails = null;
		try {
			userDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(username);
		} catch (UsernameNotFoundException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found.");
			return;
		}
		if (jwtUtil.validateToken(accessToken, userDetails.getUsername())) {

			/*
				NOTE: Oauth2AuthenticationToken 역시 내부적으로 Credential을 ""로 이용.
			 */
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, "", userDetails.getAuthorities());

			// SecurityContext에 인증 정보 설정
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		logger.debug("passed!");

		filterChain.doFilter(request, response); // 요청 처리 계속
	}

}