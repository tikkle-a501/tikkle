package com.taesan.tikkle.domain.chat.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
	// 마지막 메시지 전송 시간(MongoDB)
	private LocalDateTime lastMsgTime;

	public DetailChatroomResponse(UUID roomId, String partner) {
		this.roomId = roomId;
		this.partner = partner;
	}
}
