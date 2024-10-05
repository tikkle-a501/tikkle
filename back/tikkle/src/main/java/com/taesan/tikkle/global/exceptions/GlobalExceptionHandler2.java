package com.taesan.tikkle.global.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler2 {

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<Object> handleJsonMappingException(JsonMappingException ex) {
        // 예외 발생 시 로그 남기기
        ex.printStackTrace();

        // 오류 발생 위치 정보 가져오기
        StringBuilder errorMessage = new StringBuilder("JSON Mapping Error: " + ex.getOriginalMessage());

        // 오류 발생한 JSON 필드 경로 추가
        if (!ex.getPath().isEmpty()) {
            errorMessage.append(" at ");
            ex.getPath().forEach(reference -> errorMessage.append(reference.getFieldName()).append(" "));
        }

        // 오류 메시지 반환
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }
}

