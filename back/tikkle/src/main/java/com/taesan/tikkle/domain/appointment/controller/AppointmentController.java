package com.taesan.tikkle.domain.appointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.appointment.dto.request.CreateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.request.UpdateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.service.AppointmentService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
	@Autowired
	private static AppointmentService appointmentService;

	@PostMapping("")
	public ResponseEntity<Void> createAppointment(@RequestBody CreateAppointmentRequest request){
		appointmentService.createAppointment(request);
		return ResponseEntity.ok().build();
	}

	@PutMapping("")
	public ResponseEntity<Void> updateAppointment(@RequestBody UpdateAppointmentRequest request){
		appointmentService.updateAppointment(request);
		return ResponseEntity.ok().build();
	}
}
