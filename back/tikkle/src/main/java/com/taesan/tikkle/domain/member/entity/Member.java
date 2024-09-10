package com.taesan.tikkle.domain.member.entity;

import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.chat.entity.Chatroom;
import com.taesan.tikkle.domain.organization.entity.Organization;
import com.taesan.tikkle.domain.review.entity.Review;
import com.taesan.tikkle.global.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member extends AuditableEntity {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@ManyToOne
	@JoinColumn(name = "organization_id", nullable = false)
	private Organization organization;

	@OneToOne(mappedBy = "member")
	private Account account;

	@OneToMany(mappedBy = "member")
	private List<Board> boards;

	@OneToMany(mappedBy = "sender")
	private List<Review> sentReviews;

	@OneToMany(mappedBy = "receiver")
	private List<Review> receivedReviews;

	@OneToMany(mappedBy = "performer")
	private List<Chatroom> chatrooms;

	@Column(length = 64)
	private String name;

	@Column(length = 64)
	private String nickname;

	@Column(length = 128)
	private String email;
}
