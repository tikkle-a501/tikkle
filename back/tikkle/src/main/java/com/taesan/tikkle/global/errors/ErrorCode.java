package com.taesan.tikkle.global.errors;

public enum ErrorCode {

	/*
		TODO: 개발 과정에서 지속적인 추가 필요
	 */

    AUTH_ILL_REQUEST(1701, "요청이 유효하지 않습니다."),
    AUTH_ILL_REFRESH(1710, "올바르지 않은 Refresh Token 입니다."),
    AUTH_JWT_EXPIRED(1711, "올바르지 않은 JWT 형식입니다."),
    MEMBER_NOT_FOUND(1704, "존재하지 않는 회원입니다."),
    APPOINTMENT_NOT_AUTHORIZED(1200, "약속 참여자가 아닙니다."),
    APPOINTMENT_NOT_FOUND(1204, "약속이 존재하지 않습니다."),
    CHATROOM_NOT_FOUND(1404, "채팅방이 존재하지 않습니다."),
    CHATROOM_NOT_AUTHORIZED(1400, "채팅방 참여자가 아닙니다."),
    CHATROOM_EXISTS(1409, "이미 존재한 채팅방입니다."),
    BOARD_NOT_FOUND(1304, "게시글이 존재하지 않습니다.");

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

