package com.taesan.tikkle.domain.appointment.entity;

import java.sql.Timestamp;
import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.chat.entity.Chatroom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@ManyToOne
	@JoinColumn(name = "room_id", nullable = false)
	private Chatroom room;

	@Column(name = "appt_time")
	private Timestamp apptTime;

	@Column(name = "time_qnt", columnDefinition = "TINYINT UNSIGNED")
	private Integer timeQnt;
}

