package com.taesan.tikkle.domain.chat.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateChatroomRequest {
	private UUID boardId;
}
