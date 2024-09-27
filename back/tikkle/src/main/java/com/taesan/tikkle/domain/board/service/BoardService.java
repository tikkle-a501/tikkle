package com.taesan.tikkle.domain.board.service;

import com.taesan.tikkle.domain.board.dto.request.BoardRequest;
import com.taesan.tikkle.domain.board.dto.response.BoardResponse;
import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
