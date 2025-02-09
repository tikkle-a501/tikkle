package com.taesan.tikkle.domain.chat.dto.response;

import com.taesan.tikkle.domain.chat.entity.Chatroom;
import com.taesan.tikkle.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class EnterChatroomResponse {
	private List<ChatResponse> chats;
	private UUID senderId;
	private String partnerName;
	private byte[] partnerImage;
	// 방 상태
	private String status;
	private String boardTitle;
	private UUID boardId;
	private UUID writerId;
	private boolean isDeleted;

	@Builder
	public EnterChatroomResponse(List<ChatResponse> chats, UUID senderId, String partnerName, byte[] partnerImage,
		String status, String boardTitle, UUID boardId, UUID writerId, boolean isDeleted) {
		this.chats = chats;
		this.senderId = senderId;
		this.partnerName = partnerName;
		this.partnerImage = partnerImage;
		this.status = status;
		this.boardTitle = boardTitle;
		this.boardId = boardId;
		this.writerId = writerId;
		this.isDeleted = isDeleted;
	}

	public static EnterChatroomResponse from(List<ChatResponse> chats, Chatroom chatroom, Member partner,
		byte[] partnerProfileImage) {
		return EnterChatroomResponse.builder()
			.chats(chats)
			.senderId(chatroom.getBoard().getMember().getId())
			.partnerName(partner.getName())
			.partnerImage(partnerProfileImage)
			.status(chatroom.getBoard().getStatus())
			.boardTitle(chatroom.getBoard().getTitle())
			.boardId(chatroom.getBoard().getId())
			.writerId(chatroom.getBoard().getMember().getId())
			.isDeleted(chatroom.getBoard().isDeleted())
			.build();
	}
}
