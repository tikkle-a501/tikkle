package com.taesan.tikkle.domain.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.taesan.tikkle.domain.chat.entity.ChatMessage;

@Controller
public class ChatController {
	@Autowired
	private KafkaTemplate<String,String> kafkaTemplate;

	// TODO : 하드코딩 chatroom ID 해결 / KafkaProducer로 migration 필요
	@MessageMapping("/sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage) {
		kafkaTemplate.send("chatroom." + chatMessage.getChatRoomId().toString(), chatMessage.getContent());
		System.out.println("Received message from client: " + chatMessage.getContent());
	}
}
