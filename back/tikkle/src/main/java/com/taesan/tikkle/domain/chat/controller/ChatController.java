package com.taesan.tikkle.domain.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.chat.entity.ChatMessage;
import com.taesan.tikkle.domain.chat.service.KafkaConsumer;
import com.taesan.tikkle.domain.chat.service.KafkaProducer;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private ObjectMapper objectMapper;

//    @MessageMapping("/sendMessage")
//    public void sendMessage(@Payload ChatMessage chatMessage, @AuthedUsername UUID memberId) {
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@" + chatMessage);
//        // 로그로 메시지와 멤버 ID 출력
//        logger.info("ChatMessage 형태 : {}", chatMessage);
//        logger.info("맴버 아이디 : {}", memberId);
//        // 메시지 전송
//        kafkaProducer.sendMessage(chatMessage, memberId);
//    }

    @MessageMapping("/sendMessage")
    public void sendMessage(Message<?> message, @AuthedUsername UUID memberId) throws JsonProcessingException {
        byte[] payloadBytes = (byte[]) message.getPayload();
        String payload = new String(payloadBytes, StandardCharsets.UTF_8);
        System.out.println("Received raw payload: " + payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

        // 서비스 레이어로 메시지를 넘겨서 처리
        kafkaProducer.sendMessage(chatMessage, memberId);
    }
}
