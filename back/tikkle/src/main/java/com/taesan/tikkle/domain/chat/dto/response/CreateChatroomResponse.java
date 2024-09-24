package com.taesan.tikkle.domain.chat.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatroomResponse {
	private UUID chatRoomId;
}
