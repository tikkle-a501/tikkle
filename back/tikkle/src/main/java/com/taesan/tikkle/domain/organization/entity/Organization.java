package com.taesan.tikkle.domain.organization.entity;

import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.manager.entity.Manager;
import com.taesan.tikkle.global.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organizations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization extends AuditableEntity {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@Column(length = 32)
	private String paymentPolicy;

	@Column(length = 64)
	private String domainAddr;

	@Column(columnDefinition = "TINYINT")
	private Integer provider;

	@OneToOne
	@JoinColumn(name = "manager_id")
	private Manager manager;

	@Column(length = 32)
	private String name;

	@Builder
	public Organization(String paymentPolicy, String domainAddr, Integer provider, Manager manager, String name) {
		this.paymentPolicy = paymentPolicy;
		this.domainAddr = domainAddr;
		this.provider = provider;
		this.manager = manager;
		this.name = name;
	}
}
