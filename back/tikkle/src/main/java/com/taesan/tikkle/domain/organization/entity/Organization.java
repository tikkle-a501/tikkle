package com.taesan.tikkle.domain.organization.entity;

import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.manager.entity.Manager;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.global.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "organizations")
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

	@OneToMany(mappedBy = "organization")
	private List<Member> members;

	@OneToOne
	@JoinColumn(name = "manager_id")
	private Manager manager;

	@Column(length = 32)
	private String name;
}
