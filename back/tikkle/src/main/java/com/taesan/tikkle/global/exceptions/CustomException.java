package com.taesan.tikkle.global.exceptions;

import com.taesan.tikkle.global.errors.ErrorCode;

public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public int getCode() {
		return errorCode.getCode();
	}

	@Override
	public String getMessage() {
		return errorCode.getMessage();
	}
}

