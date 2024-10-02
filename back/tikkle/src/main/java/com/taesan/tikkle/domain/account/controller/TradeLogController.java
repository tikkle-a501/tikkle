package com.taesan.tikkle.domain.account.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taesan.tikkle.domain.account.dto.response.TradeLogFindAllResponse;
import com.taesan.tikkle.domain.appointment.service.AppointmentService;
import com.taesan.tikkle.global.annotations.AuthedUsername;
import com.taesan.tikkle.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TradeLogController {

	private final AppointmentService appointmentService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<TradeLogFindAllResponse>>> getAccountInfo(
		@AuthedUsername UUID username) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(ApiResponse.success("거래 내역 조회에 성공했습니다.", appointmentService.getAppointedBoardsByMemberId(username)));
	}
}
