package com.taesan.tikkle.domain.board.controller;

import com.taesan.tikkle.domain.board.dto.request.BoardRequest;
import com.taesan.tikkle.domain.board.dto.response.BoardResponse;
import com.taesan.tikkle.domain.board.service.BoardService;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import com.taesan.tikkle.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //전체 보드 조회
    @GetMapping("")
    public ResponseEntity<ApiResponse> getBoards() {
        return ResponseEntity.ok(ApiResponse.success("공고 리스트 받아오기 성공", boardService.getBoards()));
    }

    //보드 상세정보 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardDetail(@PathVariable UUID boardId) {
        return ResponseEntity.ok(ApiResponse.success("공고 디테일 받아오기 성공", boardService.getBoardDetail(boardId)));
    }

    //보드 생성
    @PostMapping("")
    public ResponseEntity<ApiResponse> createBoard(@RequestBody BoardRequest board, @AuthedUsername UUID username) {
        boardService.createBoard(board, username);
        return ResponseEntity.ok(ApiResponse.success("공고 작성 성공", null));
    }

    //보드 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable UUID boardId, @RequestBody BoardRequest board) {
        boardService.updateBoard(board, boardId);
        return ResponseEntity.ok().build();
    }

    //보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse> deleteBoard(@PathVariable UUID boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok(ApiResponse.success("공고 삭제 성공", null));
    }

    //보드 상태 수정
    @PatchMapping("/{boardId}/status")
    public ResponseEntity<Void> updateBoardStatus(@PathVariable UUID boardId, String status) {
        boardService.updateBoardStatus(boardId, status);
        return ResponseEntity.ok().build();
    }
}
