package com.taesan.tikkle.domain.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.taesan.tikkle.global.resolver.AuthedUsernameArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final AuthedUsernameArgumentResolver currentUserIdArgumentResolver;

	public WebConfig(AuthedUsernameArgumentResolver currentUserIdArgumentResolver) {
		this.currentUserIdArgumentResolver = currentUserIdArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentUserIdArgumentResolver);
	}
}
