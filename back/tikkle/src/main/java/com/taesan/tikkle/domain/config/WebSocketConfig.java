package com.taesan.tikkle.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setAllowedOrigins("https://j11a501.p.ssafy.io").withSockJS();
	}

	@Bean
	public WebSocketHandlerDecoratorFactory webSocketHandlerDecoratorFactory() {
		return handler -> new WebSocketHandlerDecorator(handler) {
			@Override
			public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
				// 메시지 처리 전 로그 남기기
				if (message instanceof TextMessage) {
					System.out.println("받은 메시지: " + ((TextMessage) message).getPayload());
				}
				super.handleMessage(session, message);
			}

			@Override
			public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
				// WebSocket 에러 처리 및 원본 메시지 출력
				System.err.println("WebSocket 에러 발생: " + exception.getMessage());

				if (session.isOpen()) {
					System.err.println("세션 상태: 열림");
				} else {
					System.err.println("세션 상태: 닫힘");
				}

				// 원본 메시지가 포함된 세션 정보 출력
				System.err.println("세션 정보: " + session.getId());
				System.err.println("원본 메시지 정보: " + session.getTextMessageSizeLimit());

				if (exception instanceof MessageConversionException) {
					System.err.println("MessageConversionException 발생: " + exception.getMessage());
				}

				// 예외가 발생할 때 추가적인 메시지 정보 출력
				super.handleTransportError(session, exception);
			}
		};
	}
}
