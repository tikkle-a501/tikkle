package com.taesan.tikkle.domain.chat.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(topicPattern = "chatroom.*", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        // 로그 추가: Kafka 토픽과 메시지 확인
        logger.info("채팅방 아이디 : {}, 메시지 내용 : {}", record.topic(), record.value());
        simpMessagingTemplate.convertAndSend("/topic/" + record.topic(), record.value());
    }

}
