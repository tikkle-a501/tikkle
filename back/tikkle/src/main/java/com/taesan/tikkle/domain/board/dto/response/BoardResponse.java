package com.taesan.tikkle.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponse {
    private UUID boardId;
    private UUID memberId;
    private String title;
    private String content;
    private Integer time;
    private String status;
    private String category;
    private Integer viewCount;
}
