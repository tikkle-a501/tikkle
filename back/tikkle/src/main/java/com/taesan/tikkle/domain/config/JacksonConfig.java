package com.taesan.tikkle.domain.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.StreamReadFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		// LocalDateTime 전역 포맷 설정
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class,
			new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer(
				DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

		mapper.registerModule(javaTimeModule);

		// 다른 설정들
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.findAndRegisterModules();
		return mapper;
	}
}