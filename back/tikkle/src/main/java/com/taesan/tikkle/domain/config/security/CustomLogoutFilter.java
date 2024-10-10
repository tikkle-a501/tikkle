package com.taesan.tikkle.domain.config.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.taesan.tikkle.domain.member.service.RedisTokenService;
import com.taesan.tikkle.global.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutFilter extends GenericFilterBean {

	private final RedisTokenService redisTokenService;
	private final JwtUtil jwtUtil;

	public CustomLogoutFilter(RedisTokenService redisTokenService, JwtUtil jwtUtil) {
		this.redisTokenService = redisTokenService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		if ("/api/v1/logout".equals(request.getRequestURI())) {

			Cookie[] cookies = request.getCookies();

			if (cookies == null) {
				chain.doFilter(request, response);
				return;
			}

			for (Cookie cookie : cookies) {
				if ("refresh_token".equals(cookie.getName())) {
					String refreshToken = cookie.getValue();
					String username = SecurityContextHolder.getContext().getAuthentication().getName();
					String newRefreshToken = jwtUtil.generateRefreshToken(new HashMap<>(), username);
					// Redis에서 Refresh Token을 무효화하고, 새로운 토큰을 발급
					redisTokenService.deleteRefreshToken(username);
					redisTokenService.storeRefreshToken(UUID.fromString(username), newRefreshToken,
						1000 * 60 * 60 * 24); // 회전
				}
			}

		}

		chain.doFilter(request, response);
	}

}
