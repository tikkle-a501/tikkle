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
import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;
import com.taesan.tikkle.global.response.ApiResponse;

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
	public UUID createAppointment(CreateAppointmentRequest request, UUID memberId) {
		Chatroom chatroom = chatroomRepository.findById(request.getRoomId())
			.orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
		Appointment appointment = new Appointment(chatroom, request.getAppTime(), request.getTimeQnt());
		appointmentRepository.save(appointment);
		// TODO : Board Status 무슨 String으로 설정할지 / 삭제하지 않고 생성하기 호출할 땐 어떻게 해야하는가
		boardRepository.findById(chatroom.getBoard().getId())
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND))
			.setStatusActive();
		List<Appointment> appointments = chatroom.getAppointments();
		appointments.get(appointments.size()-1).softDelete();
		return appointment.getId();
	}

	@Transactional
	public void deleteAppointment(UUID appointmentId, UUID memberId) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
			.orElseThrow(() -> new CustomException(ErrorCode.APPOINTMENT_NOT_FOUND));
		if (memberId.equals(appointment.getRoom().getWriter().getId())) {
			appointment.softDelete();
		} else {
			throw new CustomException(ErrorCode.APPOINTMENT_NOT_AUTHORIZED);
		}
	}

	@Transactional(readOnly = true)
	public List<DetailAppointmentResponse> getAppointments(UUID memberId) {
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
			if (!appointment.isDeleted()) {
				responses.add(
					new DetailAppointmentResponse(appointment.getId(), appointment.getApptTime(),
						appointment.getTimeQnt(),
						appointment.getCreatedAt(), chatroom.getBoard().getTitle()));
			}
		}
	}

	public BriefAppointmentResponse getAppointment(UUID roomId, UUID memberId) {
		Appointment appointment = appointmentRepository.findByRoomId(roomId).orElseThrow(EntityNotFoundException::new);
		if (!memberId.equals(appointment.getRoom().getWriter().getId())) {
			throw new CustomException(ErrorCode.APPOINTMENT_NOT_AUTHORIZED);
		} else {
			return new BriefAppointmentResponse(appointment.getId(), appointment.getApptTime(),
				appointment.getTimeQnt());
		}
	}
}
