package com.taesan.tikkle.global.response;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.taesan.tikkle.global.exceptions.CustomException;

@ControllerAdvice
public class GlobalExceptionHandler {

	/*
		FIXME: 개발 과정에서는 이용하지 않으나, 추후 배포 시 에러 정보 내보내지 않기 위해 이용 필요.
	 */
	// @ExceptionHandler(Exception.class)
	// public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex, WebRequest request) {
	// 	ApiResponse<String> response = ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	// 		"Internal Server Error");
	// 	return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	// }

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException ex, WebRequest request) {
		ApiResponse<String> response = ApiResponse.error(ex.getCode(), ex.getMessage());
		return new ResponseEntity<>(response, ex.getHttpStatus());
	}

//	@ExceptionHandler(MessageConversionException.class)
//	public ResponseEntity<Object> handleMessageConversionException(MessageConversionException ex) {
//		// 예외 발생 시 스택 트레이스 로그 남기기
//		ex.printStackTrace();
//
//		// 예외의 루트 원인 확인
//		Throwable rootCause = ex.getRootCause();
//		String rootCauseMessage = (rootCause != null) ? rootCause.getMessage() : "없음";
//
//		// 한글로 오류 메시지 구성
//		StringBuilder errorMessage = new StringBuilder("메시지 변환 중 오류가 발생했습니다: ");
//		errorMessage.append(ex.getMessage());
//		errorMessage.append("; 원인: ").append(rootCauseMessage);
//
//		// 오류 메시지 반환
//		return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
//	}

}

