package com.taesan.tikkle.domain.rate.entity;

import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import com.taesan.tikkle.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rate extends BaseEntity {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@Column(name = "time_to_rank")
	private Integer timeToRank;

	@OneToMany(mappedBy = "rate")
	private List<ExchangeLog> exchangeLogs;

	@Builder
	public Rate(int timeToRank) {
		this.timeToRank = timeToRank;
	}
}

