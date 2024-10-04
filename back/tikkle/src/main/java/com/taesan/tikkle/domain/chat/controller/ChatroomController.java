package com.taesan.tikkle.domain.chat.controller;

import com.taesan.tikkle.domain.chat.dto.request.CreateChatroomRequest;
import com.taesan.tikkle.domain.chat.dto.response.CreateChatroomResponse;
import com.taesan.tikkle.domain.chat.dto.response.DetailChatroomResponse;
import com.taesan.tikkle.domain.chat.dto.response.EnterChatroomResponse;
import com.taesan.tikkle.domain.chat.service.ChatroomService;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import com.taesan.tikkle.global.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chatroom")
public class ChatroomController {
    @Autowired
    private ChatroomService chatroomService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateChatroomResponse>> createChatRoom(@RequestBody CreateChatroomRequest request, @AuthedUsername UUID memberId) {
        ApiResponse<CreateChatroomResponse> response = ApiResponse.success("채팅방 생성에 성공했습니다.", chatroomService.createChatroom(request, memberId));
        return ResponseEntity.ok(response);
    }
    // 	어쩌라구

    // TODO : 새 채팅에 대한 알림 처리
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<DetailChatroomResponse>>> getChatrooms(@AuthedUsername UUID memberId) {
        ApiResponse<List<DetailChatroomResponse>> response = ApiResponse.success("채팅방 목록 조회에 성공했습니다.", chatroomService.getChatrooms(memberId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<EnterChatroomResponse>> enterChatroom(@PathVariable UUID roomId, @AuthedUsername UUID memberId) {
        ApiResponse<EnterChatroomResponse> response = ApiResponse.success("채팅 불러오기에 성공했습니다.", chatroomService.enterChatroom(roomId, memberId));
        return ResponseEntity.ok(response);
    }

}
