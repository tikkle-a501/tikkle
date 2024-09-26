package com.taesan.tikkle.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardRequest {
    private String title;
    private String content;
    private Integer time;
    private String status;
    private String category;
    private Integer viewCount;
}
