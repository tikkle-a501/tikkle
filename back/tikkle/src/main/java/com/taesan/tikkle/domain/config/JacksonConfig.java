package com.taesan.tikkle.domain.config;

import com.fasterxml.jackson.core.StreamReadFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

	@Bean(name = "customObjectMapper")
	public ObjectMapper customObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature(), true);
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}
}