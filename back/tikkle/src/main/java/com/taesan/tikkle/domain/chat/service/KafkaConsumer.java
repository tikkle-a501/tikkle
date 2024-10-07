package com.taesan.tikkle.domain.chat.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topicPattern = "chatroom.*", groupId = "my-group")
	public void consume(ConsumerRecord<String, ChatMessageResponse> record) {
		// Kafka 메시지 내용은 이미 ChatMessageResponse 객체이므로 그대로 사용
		ChatMessageResponse chatMessage = record.value();
		// 로그 추가: Kafka 토픽과 메시지 확인
		logger.info("채팅방 아이디 : {}, 메시지 내용 : {}", record.topic(), chatMessage);
		// STOMP 메시지 전송
		simpMessagingTemplate.convertAndSend("/topic/chatroom." + record.topic(), chatMessage);
	}
}
