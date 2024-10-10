package com.taesan.tikkle.domain.appointment.dto.response;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodoAppointmentResponse {
	private UUID appointmentId;
	private String status;
	private String partner;
	private Timestamp startTime;
	private String title;
}
