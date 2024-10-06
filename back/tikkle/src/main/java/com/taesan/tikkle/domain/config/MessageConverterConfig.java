package com.taesan.tikkle.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class MessageConverterConfig {

    @Bean
    @Primary
    @DependsOn("objectMapper")
    public MappingJackson2MessageConverter mappingJackson2MessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        // 기존에 Bean으로 등록된 ObjectMapper를 사용하도록 설정
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
