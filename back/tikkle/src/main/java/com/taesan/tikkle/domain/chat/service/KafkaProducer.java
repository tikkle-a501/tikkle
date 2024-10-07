package com.taesan.tikkle.domain.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.chat.dto.response.ChatMessageResponse;
import com.taesan.tikkle.domain.chat.entity.Chat;
import com.taesan.tikkle.domain.chat.entity.ChatMessage;
import com.taesan.tikkle.domain.chat.repository.ChatRepository;

@Service
public class KafkaProducer {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private ObjectMapper objectMapper;

	static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	@Transactional
	public void sendMessage(ChatMessage chatMessage) {
		// Logger 생성

		Chat chat = new Chat(chatMessage.getChatroomId(), chatMessage.getSenderId(), chatMessage.getContent());
		chatRepository.save(chat);

		// 로그: 메시지 저장 완료
		logger.info("ChatMessage 저장 완료. ChatroomId: {}, SenderId: {}, Content: {}",
			chatMessage.getChatroomId(), chatMessage.getSenderId(), chatMessage.getContent());

		// ChatMessageResponse 객체 생성
		ChatMessageResponse response = new ChatMessageResponse(chatMessage.getContent(), chat.getTimestamp(), chatMessage.getSenderId());

		try {
			// 로그: 직렬화 시작
			logger.info("ChatMessageResponse 객체 직렬화 시작: {}", response);

			// ObjectMapper를 사용하여 ChatMessageResponse 객체를 JSON으로 직렬화
			String responseJson = objectMapper.writeValueAsString(response);

			// 로그: 직렬화된 JSON 메시지 출력
			logger.info("직렬화된 JSON: {}", responseJson);

			// 직렬화된 JSON 메시지를 Kafka에 전송
			kafkaTemplate.send("chatroom." + chatMessage.getChatroomId(), responseJson);

			// 로그: Kafka 메시지 전송 완료
			logger.info("Kafka 메시지 전송 완료. 토픽: chatroom.{}", chatMessage.getChatroomId());

		} catch (JsonProcessingException e) {
			// 로그: 직렬화 오류 발생
			logger.error("JSON 직렬화 중 오류 발생: {}", e.getMessage());
			e.printStackTrace();
		}
	}
}
