package com.taesan.tikkle.domain.appointment.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.taesan.tikkle.domain.appointment.dto.request.CreateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.request.UpdateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.response.BriefAppointmentResponse;
import com.taesan.tikkle.domain.appointment.dto.response.DetailAppointmentResponse;
import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.appointment.repository.AppointmentRepository;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.chat.entity.Chatroom;
import com.taesan.tikkle.domain.chat.repository.ChatroomRepository;

import jakarta.persistence.EntityNotFoundException;

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
		UUID curMember = UUID.fromString("74657374-0000-0000-0000-000000000000");
		Appointment appointment = appointmentRepository.findById(appointmentId)
			.orElseThrow(EntityNotFoundException::new);
		// TODO : 검증이 필요한지 고민
		if (curMember.equals(appointment.getRoom().getWriter().getId())) {
			appointment.softDelete();
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자가 아닙니다.");
		}
	}

	@Transactional(readOnly = true)
	public List<DetailAppointmentResponse> getAppointments() {
		// TODO : refresh 토큰을 활용한 세선 로그인 UUID 가져오기
		UUID memberId = UUID.randomUUID();
		List<DetailAppointmentResponse> responses = new ArrayList<>();
		extractAppointmentFromChatroom(chatroomRepository.findByPerformerId(memberId), responses);
		extractAppointmentFromChatroom(chatroomRepository.findByWriterId(memberId), responses);
		responses.sort(Comparator.comparing(DetailAppointmentResponse::getCreatedAt).reversed());
		return responses;
	}

	private void extractAppointmentFromChatroom(List<Chatroom> chatrooms,
		List<DetailAppointmentResponse> responses) {
		for (Chatroom chatroom : chatrooms) {
			Appointment appointment = chatroom.getAppointments()
				.get(chatroom.getAppointments().size() - 1);
			if(appointment.getDeletedAt() == null) continue;
			responses.add(new DetailAppointmentResponse(appointment.getId(),appointment.getApptTime(),appointment.getTimeQnt(),appointment.getCreatedAt(),chatroom.getBoard().getTitle()));
		}
	}

	public BriefAppointmentResponse getAppointment(UUID roomId) {
		Appointment appointment = appointmentRepository.findByRoomId(roomId).orElseThrow(EntityNotFoundException::new);
		return new BriefAppointmentResponse(appointment.getId(),appointment.getApptTime(),appointment.getTimeQnt());
	}
}
