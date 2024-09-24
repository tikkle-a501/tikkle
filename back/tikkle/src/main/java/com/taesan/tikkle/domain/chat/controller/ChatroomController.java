package com.taesan.tikkle.domain.chat.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.chat.dto.request.CreateChatroomRequest;
import com.taesan.tikkle.domain.chat.dto.response.CreateChatroomResponse;
import com.taesan.tikkle.domain.chat.dto.response.EnterChatroomResponse;
import com.taesan.tikkle.domain.chat.dto.response.DetailChatroomResponse;
import com.taesan.tikkle.domain.chat.service.ChatroomService;

@RestController
@RequestMapping("/api/v1/chatroom")
public class ChatroomController {
	@Autowired
	private ChatroomService chatroomService;

	@PostMapping("")
	public ResponseEntity<CreateChatroomResponse> createChatRoom(@RequestBody CreateChatroomRequest request){
		return ResponseEntity.ok(chatroomService.createChatroom(request));
	}
	// 	어쩌라구

	// TODO : 새 채팅에 대한 알림 처리
	@GetMapping("")
	public ResponseEntity<List<DetailChatroomResponse>> getChatrooms(){
		System.out.println(chatroomService.getChatrooms());
		return ResponseEntity.ok(chatroomService.getChatrooms());
	}

	@GetMapping("{roomId}")
	public ResponseEntity<EnterChatroomResponse> enterChatroom(@PathVariable UUID roomId) {
		return ResponseEntity.ok(chatroomService.enterChatroom(roomId));
	}
}
