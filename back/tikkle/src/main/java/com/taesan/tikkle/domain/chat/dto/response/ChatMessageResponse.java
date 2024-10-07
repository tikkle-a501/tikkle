package com.taesan.tikkle.domain.chat.dto.response;

import java.time.LocalDateTime;

import javax.naming.ldap.LdapContext;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChatMessageResponse {
	private String content;
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
			", timestamp=" + timestamp +
			", senderId='" + senderId + '\'' +
			'}';
	}
}
