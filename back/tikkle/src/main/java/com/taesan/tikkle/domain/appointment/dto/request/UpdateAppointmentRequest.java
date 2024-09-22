package com.taesan.tikkle.domain.appointment.dto.request;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {
	private UUID appId;
	private Timestamp appTime;
	private Integer timeQnt;
}
