package com.taesan.tikkle.domain.appointment.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taesan.tikkle.domain.appointment.dto.request.CreateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.request.UpdateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.appointment.repository.AppointmentRepository;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.chat.repository.ChatroomRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private ChatroomRepository chatroomRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Transactional
	public void createAppointment(CreateAppointmentRequest request) {
		appointmentRepository.save(
			new Appointment(chatroomRepository.findById(request.getRoomId()).orElseThrow(EntityNotFoundException::new),
				request.getAppTime(), request.getTimeQnt()));
		boardRepository.findById(
				chatroomRepository.findById(request.getRoomId()).orElseThrow(EntityNotFoundException::new)
					.getBoard().getId())
			.orElseThrow(EntityNotFoundException::new)
			.setStatusActive();
	}

	@Transactional
	public void updateAppointment(UpdateAppointmentRequest request) {
		appointmentRepository.findById(request.getAppId())
			.orElseThrow(EntityNotFoundException::new).updateTime(request.getAppTime(), request.getTimeQnt());
	}

	@Transactional
	public void deleteAppointment(UUID appointmentId) {
		//  TODO : 세션에 저장된 아이디와 같은 지 확인 필요
		UUID curMember = UUID.randomUUID();
		Appointment appointment = appointmentRepository.findById(appointmentId)
			.orElseThrow(EntityNotFoundException::new);
		// TODO : 검증이 필요한지 고민
		if (curMember.equals(appointment.getRoom().getWriter().getId())) {
			appointment.softDelete();
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자가 아닙니다.");
		}
	}
}
