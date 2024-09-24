package com.taesan.tikkle.domain.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * TODO: 클라이언트-서버 API 연동 시 사후적 수정 필요
 */
@Configuration
public class SecurityConfig {

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		final String[] allowedOrigins = {"http://localhost:5500", "http://localhost:3000",
			"http://127.0.0.1:5500", "http://127.0.0.1:3000", "https://j11a501.p.ssafy.io", "http://localhost:9092", "http://127.0.0.1:9092"};

		final String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
		configuration.setAllowedMethods(Arrays.asList(allowedMethods));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
		CorsConfigurationSource corsConfigurationSource) throws Exception {

		http
			.cors((cors) -> cors.configurationSource(corsConfigurationSource))
			.csrf((csrf) -> csrf.disable());

		http
			.formLogin((auth) -> auth.disable());

		// TODO: 개발 환경을 위한 패턴이므로 추후 작성 필요
		String[] requestMatcherPatterns = new String[] {
			"/api/v1/chatroom/**","/ws/**", "/api/v1/chatroom", "/api/v1/chatroom/"
		};

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers(requestMatcherPatterns).permitAll()
//				.requestMatchers("/api/v1/manager").hasRole("MANAGER")
				.anyRequest().authenticated());

		http
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

}
