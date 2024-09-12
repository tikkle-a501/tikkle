package com.taesan.tikkle.domain.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.chat.dto.request.CreateChatroomRequest;
import com.taesan.tikkle.domain.chat.dto.reseponse.CreateChatroomResponse;
import com.taesan.tikkle.domain.chat.service.ChatroomService;

@RestController
@RequestMapping("/chatroom")
public class ChatroomController {
	@Autowired
	private static ChatroomService chatroomService;
	// TODO : 채팅방 생성
	@PostMapping("")
	public ResponseEntity<CreateChatroomResponse> createChatRoom(@RequestBody CreateChatroomRequest request){
		return ResponseEntity.ok(chatroomService.createChatroom(request));
	}
	// TODO : 채팅방 리스트 조회

	// TODO : 채팅방 입장
}
