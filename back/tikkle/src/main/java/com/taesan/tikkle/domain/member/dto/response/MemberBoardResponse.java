package com.taesan.tikkle.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberBoardResponse {
    private UUID boardId;
    private String title;
    private String content;
    private String status;
    private Integer time;
    private LocalDateTime createdAt;
}
