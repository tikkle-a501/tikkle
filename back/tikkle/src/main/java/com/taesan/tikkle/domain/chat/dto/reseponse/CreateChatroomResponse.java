package com.taesan.tikkle.domain.chat.dto.reseponse;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatroomResponse {
	private UUID chatRoomId;
}
