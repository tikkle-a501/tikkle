package com.taesan.tikkle.domain.chat.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.chat.dto.request.CreateChatroomRequest;
import com.taesan.tikkle.domain.chat.dto.reseponse.CreateChatroomResponse;
import com.taesan.tikkle.domain.chat.entity.Chatroom;
import com.taesan.tikkle.domain.chat.repository.ChatroomRepository;
import com.taesan.tikkle.domain.member.entity.Member;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ChatroomService {
	@Autowired
	private static ChatroomRepository chatroomRepository;

	@Autowired
	private static BoardRepository boardRepository;

	@Transactional
	public CreateChatroomResponse createChatroom(CreateChatroomRequest request) {
		UUID boardId = request.getBoardId();
		Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
		Member writer = board.getMember();
		if(chatroomRepository.findByBoardId(boardId).isPresent()) {
			throw new EntityExistsException();
		}
		// TODO : 현재 세션에 로그인한 회원 가져와야함
		Chatroom chatroom = new Chatroom(board,writer,writer);
		chatroomRepository.save(chatroom);
		return new CreateChatroomResponse(chatroom.getId());
	}
}
