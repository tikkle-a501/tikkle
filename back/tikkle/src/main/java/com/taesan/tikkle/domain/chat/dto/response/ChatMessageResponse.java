package com.taesan.tikkle.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageResponse {
	private String content;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;
	private String senderId;

	@JsonCreator
	public ChatMessageResponse(
		@JsonProperty("content") String content,
		@JsonProperty("timestamp") LocalDateTime timestamp,
		@JsonProperty("senderId") String senderId) {
		this.content = content;
		this.timestamp = timestamp;
		this.senderId = senderId;
	}

	@Override
	public String toString() {
		return "ChatMessageResponse{" +
			"content='" + content + '\'' +
			", timestamp='" + timestamp + '\'' +
			", senderId='" + senderId + '\'' +
			'}';
	}
}
