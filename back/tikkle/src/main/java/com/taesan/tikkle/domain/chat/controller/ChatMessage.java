package com.taesan.tikkle.domain.chat.controller;

import java.util.UUID;

import lombok.Data;

@Data
public class ChatMessage {
	private UUID chatRoomId;
	private UUID senderId;
	private String content;
}
