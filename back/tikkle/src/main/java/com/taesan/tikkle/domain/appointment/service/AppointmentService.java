package com.taesan.tikkle.domain.appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		Appointment appointment = appointmentRepository.findById(request.getAppId())
			.orElseThrow(EntityNotFoundException::new);
		appointment.updateTime(request.getAppTime(), request.getTimeQnt());
	}
}
