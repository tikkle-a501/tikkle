package com.taesan.tikkle.domain.chat.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
	private String content;
	private LocalDateTime timestamp;
	private String senderId;
}
