package com.taesan.tikkle.domain.appointment.dto.request;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {
	private UUID appointmentId;
	private Timestamp appTime;
	private Integer timeQnt;
}
