package com.taesan.tikkle.domain.review.entity;

import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	private Member sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id", nullable = false)
	private Member receiver;

	@Column(length = 16)
	private String type;

	public Review(Member sender, Member receiver, String type) {
		this.sender = sender;
		this.receiver = receiver;
		this.type = type;
	}
	// 일단 필요한 지 모르겠어서 주석 처리
	// @Column(length = 16)
	// private String content;
}

