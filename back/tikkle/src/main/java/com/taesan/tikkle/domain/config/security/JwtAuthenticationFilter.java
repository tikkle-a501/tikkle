package com.taesan.tikkle.domain.config.security;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.member.service.CustomOAuth2UserService;
import com.taesan.tikkle.domain.member.service.CustomUserDetailsService;
import com.taesan.tikkle.domain.member.service.RedisTokenService;
import com.taesan.tikkle.global.response.ApiResponse;
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
	private final RedisTokenService redisTokenService;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		logger.debug("cookies: {}", request.getCookies());

		String accessCookieValue = null;
		String refreshCookieValue = null;

		// 쿠키에서 "Authorization", "refresh_token" 쿠키를 찾음
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("Authorization".equals(cookie.getName())) {
					accessCookieValue = cookie.getValue();
				} else if ("refresh_token".equals(cookie.getName())) {
					refreshCookieValue = cookie.getValue();
				}
			}
		}

		// 둘 중 하나라도 없으면 OAuth 인증 필요
		if (accessCookieValue == null || refreshCookieValue == null) {
			filterChain.doFilter(request, response);
			logger.debug("jwt token null!");
			return;
		}

		String username = null;
		String accessToken = null;
		String refreshToken = null;

		if (!accessCookieValue.startsWith("Bearer+")) {
			ApiResponse<Object> errorResponse = ApiResponse.error(1711, "올바르지 않은 JWT 토큰 형식입니다.");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonResponse);
			return;
		}

		accessToken = accessCookieValue.substring(7);
		refreshToken = refreshCookieValue;
		logger.debug("accessToken: {}", accessToken);
		try {
			username = jwtUtil.extractUsername(accessToken);
		} catch (Exception e) {
			ApiResponse<Object> errorResponse = ApiResponse.error(1711, "올바르지 않은 JWT 토큰 형식입니다.");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonResponse);
			return;
		}

		if (username == null || !username.equals(jwtUtil.extractUsername(refreshToken))) {
			ApiResponse<Object> errorResponse = ApiResponse.error(1711, "올바르지 않은 JWT 토큰 형식입니다.");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonResponse);
			return;
		}

		CustomUserDetails userDetails = null;
		try {
			userDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(username);
		} catch (UsernameNotFoundException e) {
			ApiResponse<Object> errorResponse = ApiResponse.error(1704, "존재하지 않는 회원입니다.");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonResponse);
			return;
		}

		if (jwtUtil.validateToken(accessToken, userDetails.getUsername())) {
			/*
				NOTE: Oauth2AuthenticationToken 역시 내부적으로 Credential을 ""로 이용.
			 */
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, "", userDetails.getAuthorities());

			// SecurityContext에 인증 정보 설정
			logger.debug("auth username: {}", ((CustomUserDetails)authentication.getPrincipal()).getMemberId());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			logger.debug("passed!");
			filterChain.doFilter(request, response);
		}

		if (!refreshToken.equals(redisTokenService.getRefreshToken(userDetails.getUsername()))) {
			ApiResponse<Object> errorResponse = ApiResponse.error(1710, "올바르지 않은 RefreshToken 입니다");
			String jsonResponse = objectMapper.writeValueAsString(errorResponse);

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonResponse);
			return;
		}

		/*
			NOTE: refreshToken은 유효하나 accessToken이 유효하지 않은 경우 AccessToken 재발급
		 */
		if (jwtUtil.validateToken(refreshToken, userDetails.getUsername())) {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, "", userDetails.getAuthorities());

			// SecurityContext에 인증 정보 설정
			logger.debug("auth username: {}", ((CustomUserDetails)authentication.getPrincipal()).getMemberId());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			logger.debug("passed!");

			String newAccessToken = jwtUtil.generateToken(new HashMap<>(), userDetails.getUsername());

			ResponseCookie jwtCookie
				= ResponseCookie.from("Authorization", newAccessToken)
				.maxAge(60 * 60)
				.path("/")
				.httpOnly(false)
				.secure(false)
				.build();

			response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

		} else {
			filterChain.doFilter(request, response);
			return;
		}

		/*
			NOTE: Refresh Token이 유효하면서, 유효기간의 절반이 지난 경우, 추가 처리.
		 */
		if (jwtUtil.isHalfLifePassed(refreshToken)) {
			String newRefreshToken = jwtUtil.generateRefreshToken(new HashMap<>(), userDetails.getUsername());

			ResponseCookie refreshTokenCookie
				= ResponseCookie.from("Authorization", newRefreshToken)
				.maxAge(60 * 60 * 24)
				.path("/")
				.httpOnly(false)
				.secure(false)
				.build();

			response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
			redisTokenService.storeRefreshToken(userDetails.getMemberId(), newRefreshToken, 1000 * 60 * 60 * 24);
		}

		filterChain.doFilter(request, response); // 요청 처리 계속
	}

}