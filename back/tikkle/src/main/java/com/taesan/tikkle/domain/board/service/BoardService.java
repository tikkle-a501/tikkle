package com.taesan.tikkle.domain.board.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.board.dto.request.BoardRequest;
import com.taesan.tikkle.domain.board.dto.response.BoardResponse;
import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private MemberRepository memberRepository;

	public List<BoardResponse> getBoards() {
		List<Board> boards = boardRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
		List<BoardResponse> responses = new ArrayList<>();

		// Board 객체를 BoardResponse 객체로 변환
		for (Board board : boards) {
			BoardResponse response = new BoardResponse(
				board.getId(),
				board.getMember().getId(),
				board.getMember().getName(),
				board.getTitle(),
				board.getContent(),
				board.getTime(),
				board.getStatus(),
				board.getCategory(),
				board.getViewCount(),
				board.getCreatedAt()
			);
			responses.add(response);
		}
		return responses;
	}

	public BoardResponse getBoardDetail(UUID boardId) {

		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + boardId));
		return new BoardResponse(board.getId(),
			board.getMember().getId(),
			board.getMember().getName(),
			board.getTitle(),
			board.getContent(),
			board.getTime(),
			board.getStatus(),
			board.getCategory(),
			board.getViewCount(),
			board.getCreatedAt());
	}

	public void createBoard(BoardRequest request, UUID memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		Board board = request.toBoard(member);
		boardRepository.save(board);

	}

	@Transactional
	public void updateBoard(BoardRequest request, UUID boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + boardId));

		// request 데이터를 통해 board를 업데이트
		board.changeTitle(request.getTitle());
		board.changeContent(request.getContent());
		board.changeTime(request.getTime());
		board.changeStatus(request.getStatus());
		board.changeCategory(request.getCategory());

		// 저장은 @Transactional로 인해 자동으로 처리됨 (dirty checking)
	}

	@Transactional
	public void updateBoardStatus(UUID boardId, String status) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다: " + boardId));

		board.changeStatus(status);
	}

	@Transactional
	public void deleteBoard(UUID boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow();
		board.softDelete();
	}

	public List<BoardResponse> getBoardsByKeyword(String keyword) {
		// 제목에 keyword가 포함된 게시물 검색
		List<Board> titleMatches = boardRepository.findByTitleContainingAndDeletedAtIsNull(keyword);

		// 내용에 keyword가 포함된 게시물 검색
		List<Board> contentMatches = boardRepository.findByContentContainingAndDeletedAtIsNull(keyword);

		// 제목과 내용에서 검색된 게시물을 하나의 Set으로 합침 (중복 제거)
		Set<Board> combinedResults = new HashSet<>(titleMatches);
		combinedResults.addAll(contentMatches);

		// Board 엔티티를 BoardResponse DTO로 변환하고, 최신순으로 정렬
		List<BoardResponse> responses = combinedResults.stream()
			.map(board -> new BoardResponse(
				board.getId(),
				board.getMember().getId(),
				board.getMember().getName(),
				board.getTitle(),
				board.getContent(),
				board.getTime(),
				board.getStatus(),
				board.getCategory(),
				board.getViewCount(),
				board.getCreatedAt()
			))
			.sorted(Comparator.comparing(BoardResponse::getCreatedAt).reversed())  // 최신순으로 정렬
			.collect(Collectors.toList());

		return responses;
	}
}
