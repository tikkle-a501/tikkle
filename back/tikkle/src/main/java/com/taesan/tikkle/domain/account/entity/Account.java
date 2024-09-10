package com.taesan.tikkle.domain.account.entity;

import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@OneToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@OneToMany(mappedBy = "recAccount")
	private List<TradeLog> receivedTrades;

	@OneToMany(mappedBy = "reqAccount")
	private List<TradeLog> requestedTrades;

	@OneToMany(mappedBy = "account")
	private List<ExchangeLog> exchangeLogs;

	@Column(name = "time_qnt", columnDefinition = "TINYINT UNSIGNED")
	private Integer timeQnt;

	@Column(name = "ranking_point", columnDefinition = "TINYINT UNSIGNED")
	private Integer rankingPoint;
}

