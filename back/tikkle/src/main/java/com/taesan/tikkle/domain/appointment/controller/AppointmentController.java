package com.taesan.tikkle.domain.appointment.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.appointment.dto.request.CreateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.request.UpdateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.response.BriefAppointmentResponse;
import com.taesan.tikkle.domain.appointment.dto.response.DetailAppointmentResponse;
import com.taesan.tikkle.domain.appointment.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/appointment")
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

	@DeleteMapping("{appointmentId}")
	public ResponseEntity<Void> deleteAppointment(@PathVariable UUID appointmentId){
		appointmentService.deleteAppointment(appointmentId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("")
	public ResponseEntity<List<DetailAppointmentResponse>> getAppointments(){
		return ResponseEntity.ok(appointmentService.getAppointments());
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<BriefAppointmentResponse> getAppointment(@PathVariable UUID roomId){
		return ResponseEntity.ok(appointmentService.getAppointment(roomId));
	}
}
