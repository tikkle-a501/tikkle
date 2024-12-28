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
		// 게시글 조회
		Board board = boardRepository.findById(request.getBoardId())
				.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

		// 게시글 작성자 조회
		Member writer = board.getMember();

		// 약속 참여자 조회
		Member performer = memberRepository.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		// 동일한 조건의 채팅방이 존재하는 지 조회
		Optional<Chatroom> existingChatroom = chatroomRepository.findByBoardIdAndWriterIdAndPerformerId(
				request.getBoardId(), writer.getId(), performer.getId());

		// 이미 존재한 경우 기존 채팅방 ID 반환
		if (existingChatroom.isPresent()) {
			return new CreateChatroomResponse(existingChatroom.get().getId());
		}

		// 존재하지 않은 경우 새로운 채팅방 생성
		Chatroom chatroom = new Chatroom(board, performer, writer);

		// MariaDB에 채팅방 저장
		chatroomRepository.save(chatroom);

		// 생성된 채팅방 ID 반환
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

				responses.add(new DetailChatroomResponse(
						chatroom.getId(),
						partnerName,
						partnerImage,
						lastSender.getName(),
						lastChat.getContent(),
						lastChat.getTimestamp()
				));
			} else {
				responses.add(new DetailChatroomResponse(chatroom.getId(), partnerName, partnerImage));
			}
		}
	}

	@Transactional
	public EnterChatroomResponse enterChatroom(UUID roomId, UUID memberId) {
		Logger logger = LoggerFactory.getLogger(this.getClass());

		// 로그: roomId와 memberId 출력
		logger.info("채팅방 입장 시도 - roomId: {}, memberId: {}", roomId, memberId);

		Chatroom chatroom = chatroomRepository.findById(roomId)
				.orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

		// 로그: chatroom 정보 출력
		logger.info("채팅방 찾음 - roomId: {}, 작성자: {}, 참가자: {}", roomId, chatroom.getWriter().getId(),
				chatroom.getPerformer().getId());

		if (!memberId.equals(chatroom.getWriter().getId()) && !memberId.equals(chatroom.getPerformer().getId())) {
			logger.warn("채팅방 입장 권한 없음 - memberId: {}", memberId);
			throw new CustomException(ErrorCode.CHATROOM_NOT_AUTHORIZED);
		}

		// 로그: 채팅 조회 시도
		logger.info("채팅 내역 조회 시도 - roomId: {}", roomId);
		List<Chat> cs = chatRepository.findByChatroomIdOrderByTimestampAsc(roomId.toString());
		logger.info("변환 전 Chats : {} ", cs);
		List<ChatResponse> chats = chatRepository.findByChatroomIdOrderByTimestampAsc(roomId.toString())
				.stream()
				.map(
						chat -> new ChatResponse((UUID.fromString(chat.getSenderId())), chat.getContent(), chat.getTimestamp()))
				.collect(Collectors.toList());

		// 로그: 조회된 채팅 목록의 크기 출력
		logger.info("조회된 채팅 수: {}", chats.size());

		// 로그: 채팅 데이터가 비어 있을 때 경고 로그 출력
		if (chats.isEmpty()) {
			logger.warn("채팅 내역이 없습니다 - roomId: {}", roomId);
		} else {
			logger.info("채팅 내역: {}", chats);
		}

		Member partner = chatroom.getWriter().getId().equals(memberId) ? chatroom.getPerformer() :
				chatroom.getWriter();

		return new EnterChatroomResponse(chats, chatroom.getBoard().getMember().getId(),
				partner.getName(), fileService.getProfileImage(partner.getId()), chatroom.getBoard().getStatus(),
				chatroom.getBoard().getTitle(), chatroom.getBoard().getId(), chatroom.getBoard().getMember().getId(),
				chatroom.getBoard().isDeleted());
	}

}
