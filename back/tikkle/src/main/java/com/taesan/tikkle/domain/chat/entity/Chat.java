package com.taesan.tikkle.domain.chat.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "messages")
@Getter
@Setter
public class Chat {
	@Id
	private ObjectId id;  // MongoDB가 자동으로 ObjectId 생성

	private UUID chatroomId;
	private UUID senderId;
	private String content;

	@CreatedDate
	private LocalDateTime timestamp;

	public Chat(UUID chatroomId, UUID senderId, String content) {
		this.chatroomId = chatroomId;
		this.senderId = senderId;
		this.content = content;

	}
}