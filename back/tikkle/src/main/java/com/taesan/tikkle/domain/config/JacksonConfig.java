package com.taesan.tikkle.domain.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@Configuration
public class JacksonConfig {

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		// DateTimeFormatter 정의
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// LocalDateTime 전역 포맷 설정
		JavaTimeModule javaTimeModule = new JavaTimeModule();

		// 직렬화기 추가 (LocalDateTime -> String)
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

		// 역직렬화기 추가 (String -> LocalDateTime)
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));

		// 모듈 등록
		mapper.registerModule(new Jdk8Module());
		mapper.registerModule(new ParameterNamesModule());
		mapper.registerModule(javaTimeModule);

		// 다른 설정들
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// mapper.findAndRegisterModules();

		return mapper;
	}
}