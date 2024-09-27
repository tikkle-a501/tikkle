package com.taesan.tikkle.global.errors;

public enum ErrorCode {

	/*
		TODO: 개발 과정에서 지속적인 추가 필요
	 */
	AUTH_JWT_EXPIRED(10001, "JWT 토큰이 만료되었습니다."),
	APPOINTMENT_NOT_AUTHORIZED(1200,"약속 참여자가 아닙니다."),
	APPOINTMENT_NOT_FOUND(1204,"약속이 존재하지 않습니다."),
	CHATROOM_NOT_FOUND(1404,"채팅방이 존재하지 않습니다."),
	BOARD_NOT_FOUND(1304,"게시글이 존재하지 않습니다.");

	private final int code;
	private final String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}

