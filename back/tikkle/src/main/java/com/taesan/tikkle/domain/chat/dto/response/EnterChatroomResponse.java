package com.taesan.tikkle.domain.chat.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterChatroomResponse {
	private List<ChatResponse> chats;
	private String partnerName;
	// 방 상태
	private String status;
	private String boardTitle;
	private UUID boardId;

}
