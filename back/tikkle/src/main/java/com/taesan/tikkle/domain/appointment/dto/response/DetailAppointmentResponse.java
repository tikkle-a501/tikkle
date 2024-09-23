package com.taesan.tikkle.domain.appointment.dto.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailAppointmentResponse {
	private UUID appointmentId;
	private Timestamp appointmentTime;
	private Integer timeQnt;
	private LocalDateTime createdAt;
	// appointment에서 room가져오고 room에서 board가져오기
	private String appointmentName; 
}
