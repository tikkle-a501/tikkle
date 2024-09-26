package com.taesan.tikkle.domain.config.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.taesan.tikkle.global.utils.JwtUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtOAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(JwtOAuth2SuccessHandler.class);
	private final JwtUtil jwtUtil;

	public JwtOAuth2SuccessHandler(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		logger.debug("authenticaitonHandler 도달");
		UUID memberId = oAuth2User.getAttribute("memberId");

		Map<String, Object> claims = new HashMap<>();

		String jwt = jwtUtil.generateToken(claims, memberId.toString());


		/*
			FIXME: 배포 환경에서는 setSecure를 true로 바꿔줘야 함.
		 */
		ResponseCookie jwtCookie
			= ResponseCookie.from("Authorization", "Bearer+" + jwt)
			.maxAge(60 * 60)
			.path("/")
			.httpOnly(false)
			.secure(false)
			.build();

		response.addHeader("Set-Cookie", jwtCookie.toString());
		response.sendRedirect("http://localhost:3000/home");
	}

}
