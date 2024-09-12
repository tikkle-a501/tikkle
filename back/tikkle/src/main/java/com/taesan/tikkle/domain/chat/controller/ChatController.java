package com.taesan.tikkle.domain.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.chat.entity.ChatMessage;

@Controller
public class ChatController {
	@Autowired
	private KafkaTemplate<String,String> kafkaTemplate;

	@MessageMapping("/sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage) {
		kafkaTemplate.send("chatroom." + chatMessage.getChatRoomId().toString(), chatMessage.getContent());
		System.out.println("Received message from client: " + chatMessage.getContent());
	}
}
