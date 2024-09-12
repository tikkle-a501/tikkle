package com.taesan.tikkle.domain.account.entity;

import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.global.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AuditableEntity {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "time_qnt", columnDefinition = "INTEGER")
	private Integer timeQnt;

	@Column(name = "ranking_point", columnDefinition = "TINYINT UNSIGNED")
	private Integer rankingPoint;
}

