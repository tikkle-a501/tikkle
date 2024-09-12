package com.taesan.tikkle.domain.account.entity;

import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.rate.entity.Rate;
import com.taesan.tikkle.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exchange_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeLog extends BaseEntity {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rates_id", nullable = false)
	private Rate rate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	@Column(length = 8)
	private String type;

	@Column(name = "quantity", columnDefinition = "TINYINT UNSIGNED")
	private Integer quantity;

	@Builder
	public ExchangeLog(UUID id, Rate rate, Account account, String type, Integer quantity) {
		this.id = id;
		this.rate = rate;
		this.account = account;
		this.type = type;
		this.quantity = quantity;
	}
}

