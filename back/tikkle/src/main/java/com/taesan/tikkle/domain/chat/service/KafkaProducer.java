package com.taesan.tikkle.domain.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.chat.entity.Chat;
import com.taesan.tikkle.domain.chat.entity.ChatMessage;
import com.taesan.tikkle.domain.chat.repository.ChatRepository;

import java.util.UUID;

@Service
public class KafkaProducer {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ChatRepository chatRepository;

	@Transactional
	public void sendMessage(ChatMessage chatMessage, UUID memberId) {
		kafkaTemplate.send("chatroom." + chatMessage.getChatroomId().toString(), chatMessage.getContent());
		chatRepository.save(new Chat(chatMessage.getChatroomId().toString(), memberId.toString(), chatMessage.getContent()));
	}
}
