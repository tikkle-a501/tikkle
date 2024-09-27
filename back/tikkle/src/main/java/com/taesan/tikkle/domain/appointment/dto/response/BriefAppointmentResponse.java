package com.taesan.tikkle.domain.appointment.dto.response;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BriefAppointmentResponse {
	private UUID appointmentId;
	private Timestamp appointmentTime;
	private Integer timeQnt;
}
