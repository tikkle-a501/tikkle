package com.taesan.tikkle.domain.chat.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.chat.dto.response.ChatMessageResponse;

@Service
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	// STOMP 메시지 전송을 위한 비동기 실행 스레드 풀 생성 (한 번에 5개씩 비동기 처리)
	private final ExecutorService stompExecutor = Executors.newFixedThreadPool(5);

	@KafkaListener(topicPattern = "chatroom.*", groupId = "chat-consumer-group", concurrency = "3") // 스레드 3개 사용
	public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
		// chatroomId와 message 추출
		String topic = record.topic();
		String message = record.value();

		// message 비어있는지 확인
		if (message == null || message.isBlank()) {
			logger.warn("수신한 메시지가 비어 있거나 공백입니다. Topic: {}", topic);
			ack.acknowledge(); // Kafka Offset 커밋
			return;
		}
		try {
			// ObjectMapper를 사용하여 JSON 메시지를 ChatMessageResponse 객체로 변환
			ChatMessageResponse response = objectMapper.readValue(message, ChatMessageResponse.class);
			logger.info("수신한 ChatMessageResponse: {}", response);

			// chatroomId 추출
			String chatroomId = topic.replace("chatroom.", "");

			// STOMP 전송을 비동기적으로 실행
			stompExecutor.submit(() -> {
				try {
					sendMessageToStomp(chatroomId, response);
				} catch (Exception e) {
					logger.error("STOMP 전송 중 오류 발생: {}", e.getMessage());
				}
			});
		} catch (JsonProcessingException e) {
			logger.error("JSON 역직렬화 중 오류 발생: {}, Topic: {}", e.getMessage(), topic);
			// TODO : DLQ로 메시지 이동 가능 (Kafka Dead Letter Queue 설정 필요)
			// kafkaTemplate.send("dead-letter-queue", message);
		} catch (Exception e) {
			logger.error("Kafka Consumer 처리 중 예외 발생: {}", e.getMessage());
		} finally {
			// ✅ 메시지 정상 처리 후 Kafka Offset 커밋 (중복 방지)
			ack.acknowledge();
		}
	}

	// STOMP 메시지 전송을 비동기적으로 실행
	private void sendMessageToStomp(String chatroomId, ChatMessageResponse response) {
		try {
			simpMessagingTemplate.convertAndSend("/topic/chatroom." + chatroomId, response);
			logger.info("STOMP 메시지 전송 완료: chatroom.{}", chatroomId);
		} catch (Exception e) {
			logger.error("STOMP 메시지 전송 실패: {}", e.getMessage());
		}
	}
}
