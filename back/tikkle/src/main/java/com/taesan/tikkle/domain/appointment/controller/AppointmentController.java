package com.taesan.tikkle.domain.appointment.controller;

import java.util.List;
import java.util.UUID;

import org.apache.coyote.Response;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.appointment.dto.request.CreateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.response.BriefAppointmentResponse;
import com.taesan.tikkle.domain.appointment.dto.response.DetailAppointmentResponse;
import com.taesan.tikkle.domain.appointment.dto.response.TodoAppointmentResponse;
import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.appointment.service.AppointmentService;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import com.taesan.tikkle.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	@PostMapping("")
	public ResponseEntity<ApiResponse<UUID>> createAppointment(@RequestBody CreateAppointmentRequest request,@AuthedUsername UUID memberId) {
		ApiResponse<UUID> response = ApiResponse.success("약속 생성에 성공했습니다.",appointmentService.createAppointment(request,memberId));
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{appointmentId}")
	public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable UUID appointmentId,
		@AuthedUsername UUID memberId) {
		appointmentService.deleteAppointment(appointmentId, memberId);
		return ResponseEntity.ok(ApiResponse.success("약속 삭제에 성공했습니다.", null)); // Void 타입이므로 null로
	}

	@GetMapping("")
	public ResponseEntity<ApiResponse<List<DetailAppointmentResponse>>> getAppointments(@AuthedUsername UUID memberId) {
		ApiResponse<List<DetailAppointmentResponse>> response = ApiResponse.success("약속 목록 조회에 성공했습니다.",
			appointmentService.getAppointments(memberId));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<ApiResponse<BriefAppointmentResponse>> getAppointment(@PathVariable UUID roomId,
		@AuthedUsername UUID memberId) {
		ApiResponse<BriefAppointmentResponse> response = ApiResponse.success("약속 상세보기에 성공했습니다.",
			appointmentService.getAppointment(roomId, memberId));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/todo")
	public ResponseEntity<ApiResponse<List<TodoAppointmentResponse>>> getTodoAppointments(@AuthedUsername UUID memberId){
		ApiResponse<List<TodoAppointmentResponse>> response = ApiResponse.success("",appointmentService.getTodoAppointments(memberId));
		return ResponseEntity.ok(response);
	}
}
