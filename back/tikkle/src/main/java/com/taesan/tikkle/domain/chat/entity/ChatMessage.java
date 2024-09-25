package com.taesan.tikkle.domain.chat.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessage {
	private UUID chatroomId;
	private UUID senderId;
	private String content;
}
