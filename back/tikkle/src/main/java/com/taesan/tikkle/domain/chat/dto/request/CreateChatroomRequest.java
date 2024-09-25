package com.taesan.tikkle.domain.chat.dto.request;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CreateChatroomRequest {
	private UUID boardId;
}
