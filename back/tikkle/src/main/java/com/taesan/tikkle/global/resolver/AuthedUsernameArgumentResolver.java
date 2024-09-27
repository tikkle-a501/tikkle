package com.taesan.tikkle.global.resolver;

import java.util.UUID;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.taesan.tikkle.domain.config.security.CustomUserDetails;
import com.taesan.tikkle.global.annotations.AuthedUsername;

/**
 * AuthedUsernameArgumentResolver
 *
 * Spring Security에서 인증된 사용자의 UUID Username 이용 목적.
 * Authentication의 Principal이 CustomUserDetails인 경우 동작
 *
 * {@code CustomUserDetails.CLASS}
 */
@Component
public class AuthedUsernameArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 파라미터에 이용되는 AuthedUsername이 포함되어 있는지 확인함
		return parameter.hasParameterAnnotation(AuthedUsername.class) &&
			parameter.getParameterType().equals(UUID.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
		/*
		 	NOTE:
		 	JwtAuthenticationFilter에서 Cookie에 access token이 있으면,
		 	SecurityContext에 해당 JWT decode하여 Username인 member의 id(PK) 등록
		 	올라가 있는 Authentication 객체를 가져와서 이용
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();

			// principal이 UserDetails 구현체인 경우 username 반환
			if (principal instanceof CustomUserDetails) {
				return UUID.fromString(((CustomUserDetails)principal).getUsername());
			}

			// 아래는 익명 사용자 등, Principal이 String인 경우이나 사용하지 않음.
			// } else if (principal instanceof String) {
			//
			// 	return principal;
			// }
		}
		return null; // 인증되지 않은 경우
	}
}
