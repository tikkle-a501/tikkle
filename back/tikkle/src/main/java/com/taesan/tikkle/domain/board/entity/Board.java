package com.taesan.tikkle.domain.board.entity;

import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.chat.entity.Chatroom;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.global.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends AuditableEntity {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@OneToMany(mappedBy = "board")
	private List<Chatroom> chatrooms;

	@Column(length = 64)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(name = "time", columnDefinition = "TINYINT UNSIGNED")
	private Integer time;

	@Column(length = 8)
	private String status;

	@Column(length = 8)
	private String category;

	@Column(name = "view_count")
	private Integer viewCount;
	
	// 임시로 ACTIVE 상태로 설정
	public void setStatusActive() {
		this.status = "ACTIVE";
	}
}

