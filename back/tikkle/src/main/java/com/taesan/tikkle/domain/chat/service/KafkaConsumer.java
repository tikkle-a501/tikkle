package com.taesan.tikkle.domain.chat.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.chat.dto.response.ChatMessageResponse;

@Service
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	// TODO : concurrency를 추가해 비동기적으로 kafka consumer를 처리하고 이를 STOMP 전송 시에 활용하여 병렬적으로 처리하도록 하기
	@KafkaListener(topicPattern = "chatroom.*", groupId = "")
	public void consume(ConsumerRecord<String,String> record) {
		// chatroomId와 message 추출
		String topic = record.topic();
		String message = record.value();
		// message 비어있는지 확인
		if (message == null || message.isBlank()) {
			logger.warn("메시지가 비어 있거나 공백만 있습니다.");
			return;
		}
		// Kafka에 직렬화를 하고 message를 send 하였으므로 이를 다시 역직렬화하는 과정이 필요하다.
		try {
			// ObjectMapper를 사용하여 JSON 메시지를 ChatMessageResponse 객체로 변환
			ChatMessageResponse response = objectMapper.readValue(message, ChatMessageResponse.class);
			// 로그: 수신한 메시지 출력
			logger.info("수신한 ChatMessageResponse: {}", response);
			// 토픽에서 chatroomId 추출
			String chatroomId = topic.replace("chatroom.", "");
			// STOMP로 전송
			simpMessagingTemplate.convertAndSend("/topic/chatroom." + chatroomId, response);
		} catch (JsonProcessingException e) {
			logger.error("JSON 직렬화 중 오류 발생: {}", e.getMessage());
		}
	}

}
