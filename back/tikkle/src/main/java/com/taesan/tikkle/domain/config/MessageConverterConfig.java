package com.taesan.tikkle.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.nio.charset.StandardCharsets;

@Configuration
public class MessageConverterConfig {

    @Bean(name = "customJacksonMessageConverter")
    public MappingJackson2MessageConverter customJacksonMessageConverter(@Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        System.out.println("Custom MappingJackson2MessageConverter registered with ObjectMapper: " + objectMapper);
        return converter;
    }
}
