package com.taesan.tikkle.domain.board.dto.request;

import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {
    private String title;
    private String content;
    private Integer time;
    private String status;
    private String category;
    private Integer viewCount;

    public Board toBoard(Member member) {
        return Board.builder().member(member)
                .content(this.content).title(this.title).time(this.time).status(this.status).category(this.category).viewCount(this.viewCount).build();
    }
}
