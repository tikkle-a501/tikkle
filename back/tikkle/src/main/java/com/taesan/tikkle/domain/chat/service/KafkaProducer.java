package com.taesan.tikkle.domain.chat.service;

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

	@Transactional
	public void sendMessage(ChatMessage chatMessage) {
		Chat chat = new Chat(chatMessage.getChatroomId(), chatMessage.getSenderId(), chatMessage.getContent());
		chatRepository.save(chat);
		// ChatMessageResponse 객체 생성
		ChatMessageResponse response = new ChatMessageResponse(chatMessage.getContent(), chat.getTimestamp(),
			chatMessage.getSenderId());
		try {
			// ObjectMapper를 사용하여 ChatMessageResponse 객체를 JSON으로 직렬화
			String responseJson = objectMapper.writeValueAsString(response);
			// 직렬화된 JSON 메시지를 Kafka에 전송
			kafkaTemplate.send("chatroom." + chatMessage.getChatroomId(), responseJson);
		} catch (JsonProcessingException e) {
			// 직렬화 오류 처리
			e.printStackTrace();
			// 필요하다면 예외 처리 로직 추가
		}
	}
}
