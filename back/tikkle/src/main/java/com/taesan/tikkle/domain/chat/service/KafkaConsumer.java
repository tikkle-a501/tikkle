package com.taesan.tikkle.domain.chat.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.chat.dto.response.ChatMessageResponse;

@Service
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Qualifier("objectMapper")
	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topicPattern = "chatroom.*", groupId = "my-group")
	public void consume(ConsumerRecord<String, String> record) {
		try {
			// Kafka 메시지 내용을 JSON으로 변환하여 프론트엔드가 기대하는 구조로 생성
			ChatMessageResponse chatMessage = objectMapper.readValue(record.value(), ChatMessageResponse.class);
			// 로그 추가: Kafka 토픽과 메시지 확인
			logger.info("채팅방 아이디 : {}, 메시지 내용 : {}", record.topic(), record.value());
			// STOMP 메시지 전송
			simpMessagingTemplate.convertAndSend("/topic/" + record.topic(), chatMessage);
		} catch (Exception e) {
			logger.error("메시지 처리 중 오류 발생", e);
		}
	}
}
