package com.taesan.tikkle.domain.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.chat.entity.ChatMessage;

@RestController
@RequestMapping("/chat")
public class ChatController {
	@Autowired
	private KafkaTemplate<String,String> kafkaTemplate;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/sendMessage")
	public void sendMessage(@Payload ChatMessage chatMessage){
		// TODO : ChatMessage를 MongoDB 활용하여 NoSQL 데이터베이스에 저장하는 로직 필요
		kafkaTemplate.send("chatroom-" + chatMessage.getChatRoomId().toString(),chatMessage.getContent());
	}

	@KafkaListener(topicPattern = "chatroom-*", groupId = "chat-group")
	public void listen(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		System.out.println("메시지가 전달되었습니다 : " + message + "\n 목적지는 : " + topic);
		messagingTemplate.convertAndSend("/topic/" + topic, message);
	}


}