package com.taesan.tikkle.domain.chat.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailChatroomResponse {
	private UUID roomId;
	// 대화 상대 이름
	private String partner;
	// 마지막 메시지 보낸 사람
	private String lastSender;
	// 마지막 메시지 미리보기(MongoDB)
	private String lastMsg;

	public DetailChatroomResponse(UUID roomId, String partner) {
		this.roomId = roomId;
		this.partner = partner;
	}
}
