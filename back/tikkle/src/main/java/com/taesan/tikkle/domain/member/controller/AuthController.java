package com.taesan.tikkle.domain.member.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
	@GetMapping("/api/v1/login")
	public void login(HttpServletResponse response) throws IOException {
		// OAuth2 로그인 경로로 리다이렉트
		response.sendRedirect("/oauth2/authorization/mattermost");
	}
}
