package com.taesan.tikkle.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class ChatMessage {
	private String chatroomId;
	private String senderId;
	private String content;

	@JsonCreator
	public ChatMessage(
		@JsonProperty("chatroomId") String chatroomId,
		@JsonProperty("senderId") String senderId,
		@JsonProperty("content") String content) {
		this.chatroomId = chatroomId;
		this.senderId = senderId;
		this.content = content;
	}

	@Override
	public String toString() {
		return "ChatMessage{" +
			"chatroomId='" + chatroomId + '\'' +
			", content='" + content + '\'' +
			'}';
	}
}
