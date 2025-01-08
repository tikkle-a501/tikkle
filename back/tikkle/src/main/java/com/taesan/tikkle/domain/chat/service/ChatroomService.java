package com.taesan.tikkle.domain.chat.service;

import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.chat.dto.request.CreateChatroomRequest;
import com.taesan.tikkle.domain.chat.dto.response.ChatResponse;
import com.taesan.tikkle.domain.chat.dto.response.CreateChatroomResponse;
import com.taesan.tikkle.domain.chat.dto.response.DetailChatroomResponse;
import com.taesan.tikkle.domain.chat.dto.response.EnterChatroomResponse;
import com.taesan.tikkle.domain.chat.entity.Chat;
import com.taesan.tikkle.domain.chat.entity.Chatroom;
import com.taesan.tikkle.domain.chat.repository.ChatRepository;
import com.taesan.tikkle.domain.chat.repository.ChatroomRepository;
import com.taesan.tikkle.domain.file.service.FileService;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {
  
	private static final Logger logger = LoggerFactory.getLogger(ChatroomService.class);

	private final ChatroomRepository chatroomRepository;

	private final ChatRepository chatRepository;

	private final BoardRepository boardRepository;

	private final MemberRepository memberRepository;

	private final FileService fileService;

	@Transactional
	public CreateChatroomResponse createChatroom(CreateChatroomRequest request, UUID memberId) {
		Board board = boardRepository.findById(request.getBoardId())
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
		Member writer = board.getMember();
		Member performer = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Optional<Chatroom> existingChatroom = chatroomRepository.findByBoardIdAndWriterIdAndPerformerId(
			request.getBoardId(), writer.getId(), performer.getId());

		if (existingChatroom.isPresent()) {
			return new CreateChatroomResponse(existingChatroom.get().getId());
		}

		Chatroom chatroom = new Chatroom(board, performer, writer);
		chatroomRepository.save(chatroom);
		return new CreateChatroomResponse(chatroom.getId());
	}

	@Transactional(readOnly = true)
	public List<DetailChatroomResponse> getChatrooms(UUID memberId) {
		List<DetailChatroomResponse> responses = new ArrayList<>();
		// 공통 메서드를 사용하여 writerRooms와 performerRooms 처리
		extractChatroomDetails(chatroomRepository.findByWriterId(memberId), responses, true);
		extractChatroomDetails(chatroomRepository.findByPerformerId(memberId), responses, false);
		// 가장 최신인 메세지를 갖는 채팅방을 앞으로
		responses.sort(Comparator.comparing(DetailChatroomResponse::getLastMsgTime,
			Comparator.nullsLast(Comparator.reverseOrder())));
		return responses;
	}

	private void extractChatroomDetails(List<Chatroom> chatrooms, List<DetailChatroomResponse> responses,
		boolean isWriter) {
		for (Chatroom chatroom : chatrooms) {
			Chat lastChat = chatRepository.findTopByChatroomIdOrderByTimestampDesc(chatroom.getId().toString());
			Member partner = isWriter ? chatroom.getPerformer() : chatroom.getWriter();
			String partnerName = partner.getName();

			byte[] partnerImage = fileService.getProfileImage(partner.getId());

			if (lastChat != null) {
				Member lastSender = memberRepository.findByIdAndDeletedAtIsNull(
						(UUID.fromString(lastChat.getSenderId())))
					.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

				// 대화 상대에 따라 performer와 writer 구분

				responses.add(
					new DetailChatroomResponse(chatroom.getId(), partnerName, partnerImage, lastSender.getName(),
						lastChat.getContent(), lastChat.getTimestamp()));
			} else {
				responses.add(new DetailChatroomResponse(chatroom.getId(), partnerName, partnerImage));
			}
		}
	}


	@Transactional
	public EnterChatroomResponse enterChatroom(UUID roomId, UUID memberId) {
		// roomId에 따른 chatroom 찾기
		Chatroom chatroom = findChatroomById(roomId, memberId);

		// 채팅방에 들어올 수 있는 회원인지 조회
		validateMemberAccess(chatroom, memberId);

		// 채팅 내역 조회 및 변환
		List<ChatResponse> chats = getChatHistory(roomId);

		// 상대방 정보 조회
		Member partner = getChatPartner(chatroom, memberId);

		return EnterChatroomResponse.from(chats, chatroom, partner, fileService.getProfileImage(partner.getId()));
	}

	private Chatroom findChatroomById(UUID roomId, UUID memberId) {
		logger.info("채팅방 입장 시도 - roomId: {}, memberId: {}", roomId, memberId);
		return chatroomRepository.findById(roomId)
			.map(chatroom -> {
				logger.info("채팅방 찾음 - roomId: {}, 작성자: {}, 참가자: {}",
					roomId, chatroom.getWriter().getId(), chatroom.getPerformer().getId());
				return chatroom;
			})
			.orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
	}

	private Member getChatPartner(Chatroom chatroom, UUID memberId) {
		return chatroom.getWriter().getId().equals(memberId) ? chatroom.getPerformer() : chatroom.getWriter();
	}

	private void validateMemberAccess(Chatroom chatroom, UUID memberId) {
		if (!memberId.equals(chatroom.getWriter().getId()) && !memberId.equals(chatroom.getPerformer().getId())) {
			logger.warn("채팅방 입장 권한 없음 - memberId: {}", memberId);
			throw new CustomException(ErrorCode.CHATROOM_NOT_AUTHORIZED);
		}
	}

	private List<ChatResponse> getChatHistory(UUID roomId) {
		// 채팅 내역 조회
		logger.info("채팅 내역 조회 시도 - roomId: {}", roomId);

		List<Chat> chatList = chatRepository.findByChatroomIdOrderByTimestampAsc(roomId.toString());

		// 채팅 데이터 변환
		if (chatList.isEmpty()) {
			logger.warn("채팅 내역이 없습니다 - roomId: {}", roomId);
			return Collections.emptyList();
		}

		logger.info("조회된 채팅 수: {}", chatList.size());

		return chatList.stream()
			.map(chat -> new ChatResponse(UUID.fromString(chat.getSenderId()), chat.getContent(), chat.getTimestamp()))
			.collect(Collectors.toList());
	}
  
}
