package com.taesan.tikkle.domain.chat.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.chat.dto.response.ChatMessageResponse;

import jakarta.annotation.PreDestroy;

@Service
public class KafkaConsumer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	// STOMP 메시지 전송을 위한 비동기 실행 스레드 풀 생성 (한 번에 5개씩 비동기 처리)
	private final ExecutorService stompExecutor = Executors.newFixedThreadPool(5);

	// 비동기 실행 스레드 풀 할당 시 predestory를 활용하여 메모리 누수를 방지해야함. 문제 발생 시 주석 처리 필요
	@PreDestroy
	public void shutdownExecutor() {
		stompExecutor.shutdown();
		logger.info("STOMP ExecutorService 종료 완료");
	}

	// 동적으로 비동기 실행 스레드 풀 생성하고 싶을 때는 아래 코드 사용!
	// private final ExecutorService getStompExecutor = Executors.newCachedThreadPool();
	@KafkaListener(topicPattern = "chatroom.*", groupId = "chat-consumer-group", concurrency = "5") // 스레드 5개 사용
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
		// chatroomId 추출
		String chatroomId = topic.replace("chatroom.", "");
		try {
			// ObjectMapper를 사용하여 JSON 메시지를 ChatMessageResponse 객체로 변환
			ChatMessageResponse response = objectMapper.readValue(message, ChatMessageResponse.class);
			logger.info("수신한 ChatMessageResponse: {}", response);
			// ✅ STOMP 메시지 비동기 전송
			stompExecutor.submit(() -> sendMessageToStomp(chatroomId, response));
		} catch (JsonProcessingException e) {
			logger.error("JSON 역직렬화 중 오류 발생: {}, Topic: {}", e.getMessage(), topic);
			// ✅ DLQ로 메시지 전송
			sendToDLQ(message, chatroomId);
		} catch (Exception e) {
			logger.error("Kafka Consumer 처리 중 예외 발생: {}", e.getMessage());
			// ✅ DLQ로 메시지 전송
			sendToDLQ(message, chatroomId);
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

	private void sendToDLQ(String message, String chatroomId) {
		try {
			// DLQ에 chatroomId와 message를 역직렬화하여 보내기
			String dlqMessage = objectMapper.writeValueAsString(Map.of("chatroomId", chatroomId, "message", message));
			kafkaTemplate.send("dead-letter-queue", dlqMessage);
			logger.info("DLQ로 메시지 이동 완료");
		} catch (Exception e) {
			logger.error("DLQ 전송 실패 : {}", e.getMessage());
		}
	}

	@KafkaListener(topics = "dead-letter-queue", groupId = "dlq-consumer-group")
	public void processDLQ(ConsumerRecord<String, String> record, Acknowledgment ack) {
		String message = record.value();
		try {
			// ✅ DLQ에서 가져온 JSON 메시지를 파싱하여 `chatroomId`와 `message` 추출 (TypeReference 제네릭 타입으로 JSON을 변환 가능하다.)
			Map<String, String> dlqData = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {
			});
			String chatroomId = dlqData.get("chatroomId");
			String originalMessage = dlqData.get("message");
			if (chatroomId == null || originalMessage == null) {
				throw new IllegalArgumentException("DLQ 메시지에 필요한 정보가 없습니다.");
			}
			// ✅ 원래 메시지를 다시 JSON 변환하여 STOMP 전송
			ChatMessageResponse response = objectMapper.readValue(originalMessage, ChatMessageResponse.class);
			simpMessagingTemplate.convertAndSend("/topic/chatroom." + chatroomId, response);
			logger.info("DLQ 메시지 재처리 성공: chatroomId={}, message={}", chatroomId, response);
			// ✅ 재처리 성공 시 Kafka Offset 커밋
			ack.acknowledge();
		} catch (JsonProcessingException e) {
			logger.error("DLQ 메시지 재처리 실패! JSON 오류로 인해 메시지 폐기: {}", e.getMessage());
			ack.acknowledge(); // 메시지 폐기 후 Offset 커밋
		} catch (Exception e) {
			logger.error("DLQ 메시지 재처리 실패! 오류 발생: {}", e.getMessage());
			ack.acknowledge(); // 메시지 폐기 후 Offset 커밋
		}
	}

}
