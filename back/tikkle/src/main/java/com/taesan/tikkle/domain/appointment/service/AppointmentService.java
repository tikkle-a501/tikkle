package com.taesan.tikkle.domain.appointment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.taesan.tikkle.domain.appointment.dto.request.CreateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.request.UpdateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.response.DetailAppointmentResponse;
import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.appointment.repository.AppointmentRepository;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.chat.entity.Chatroom;
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
		Chatroom chatroom = chatroomRepository.findById(request.getRoomId()).orElseThrow(EntityNotFoundException::new);
		Appointment appointment = new Appointment(chatroom, request.getAppTime(), request.getTimeQnt());
		appointmentRepository.save(appointment);
		boardRepository.findById(chatroom.getBoard().getId())
			.orElseThrow(EntityNotFoundException::new)
			.setStatusActive();
		chatroom.getAppointments().add(appointment);
	}

	@Transactional
	public void updateAppointment(UpdateAppointmentRequest request) {
		appointmentRepository.findById(request.getAppId())
			.orElseThrow(EntityNotFoundException::new)
			.updateTime(request.getAppTime(), request.getTimeQnt());
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

	@Transactional
	public List<DetailAppointmentResponse> getAppointments() {
		// TODO : refresh 토큰을 활용한 세선 로그인 UUID 가져오기
		UUID memberId = UUID.randomUUID();
		List<Chatroom> performerChatrooms = chatroomRepository.findByPerformerId(memberId);
		List<Chatroom> writerChatrooms = chatroomRepository.findByWriterId(memberId);
		List<DetailAppointmentResponse> responses = new ArrayList<>();
		for (Chatroom performerChatroom : performerChatrooms) {
			Appointment appointment = performerChatroom.getAppointments()
				.get(performerChatroom.getAppointments().size() - 1);
			responses.add(new DetailAppointmentResponse(appointment.getId(),appointment.getApptTime(),appointment.getTimeQnt(),performerChatroom.getBoard().getTitle()));
		}
		for (Chatroom writerChatroom : writerChatrooms) {
			Appointment appointment = writerChatroom.getAppointments()
				.get(writerChatroom.getAppointments().size() - 1);
			responses.add(new DetailAppointmentResponse(appointment.getId(),appointment.getApptTime(),appointment.getTimeQnt(),writerChatroom.getBoard().getTitle()));
		}
		// TODO : 약속 생성 최신순 정렬
		return responses;
	}
}
