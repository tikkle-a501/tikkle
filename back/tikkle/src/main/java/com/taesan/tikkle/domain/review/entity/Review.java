package com.taesan.tikkle.domain.review.entity;

import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
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

	@Column(length = 16)
	private String content;
}

