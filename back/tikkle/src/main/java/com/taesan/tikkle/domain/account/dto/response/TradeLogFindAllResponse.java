package com.taesan.tikkle.domain.account.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TradeLogFindAllResponse {

	@Builder
	public TradeLogFindAllResponse(Member member, String title, String content, Integer time, String status,
		LocalDateTime createdAt) {
		this.member = member;
		this.title = title;
		this.content = content;
		this.time = time;
		this.status = status;
		this.createdAt = createdAt;
	}

	private Member member;
	private String title;
	private String content;
	private Integer time;
	private String status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	public static TradeLogFindAllResponse from(Board board) {
		return TradeLogFindAllResponse.builder()
			.member(board.getMember())
			.title(board.getTitle())
			.content(board.getContent())
			.time(board.getTime())
			.status(board.getStatus())
			.createdAt(board.getCreatedAt()).build();
	}
}
