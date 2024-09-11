package com.taesan.tikkle.domain.chat.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	// @KafkaListener(topicPattern = "chatroom.*", groupId = "my-group")
	@KafkaListener(topics = "chatroom.123e4567-e89b-12d3-a456-426614174004", groupId = "my-group")
	public void consume(ConsumerRecord<String, String> record) {
		String topic = record.topic();
		String chatRoomId = topic.split("-")[1]; // 'chatroom-' 이후의 ID 추출
		simpMessagingTemplate.convertAndSend("/topic/" + chatRoomId, record.value());
		System.out.println("메시지 : " + record.value());
	}

}
