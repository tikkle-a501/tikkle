package com.taesan.tikkle.domain.appointment.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.taesan.tikkle.domain.account.dto.DepositType;
import com.taesan.tikkle.domain.account.entity.DepositLog;
import com.taesan.tikkle.domain.account.repository.DepositLogRepository;
import com.taesan.tikkle.domain.account.repository.TradeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.dto.response.TradeLogFindAllResponse;
import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.repository.AccountRepository;
import com.taesan.tikkle.domain.appointment.dto.request.CreateAppointmentRequest;
import com.taesan.tikkle.domain.appointment.dto.response.BriefAppointmentResponse;
import com.taesan.tikkle.domain.appointment.dto.response.DetailAppointmentResponse;
import com.taesan.tikkle.domain.appointment.dto.response.TodoAppointmentResponse;
import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.appointment.repository.AppointmentRepository;
import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.chat.entity.Chatroom;
import com.taesan.tikkle.domain.chat.repository.ChatroomRepository;
import com.taesan.tikkle.domain.file.service.FileService;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import com.taesan.tikkle.global.errors.ErrorCode;
import com.taesan.tikkle.global.exceptions.CustomException;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;
	@Autowired
	private ChatroomRepository chatroomRepository;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private TradeLogRepository tradeLogRepository;
	@Autowired
	private FileService fileService;
    @Autowired
    private DepositLogRepository depositLogRepository;

	public List<TodoAppointmentResponse> getTodoAppointments(UUID memberId) {
		List<Appointment> appointments = new ArrayList<>();
		List<Chatroom> performers = chatroomRepository.findByPerformerId(memberId);
		List<Chatroom> writers = chatroomRepository.findByWriterId(memberId);
		for (Chatroom chatroom : performers) {

			if (!chatroom.getAppointments().isEmpty() && !chatroom.getAppointments()
				.get(chatroom.getAppointments().size() - 1)
				.isDeleted()) {
				appointments.add(chatroom.getAppointments().get(chatroom.getAppointments().size() - 1));
			}
		}
		for (Chatroom chatroom : writers) {
			if (!chatroom.getAppointments().isEmpty() && !chatroom.getAppointments()
				.get(chatroom.getAppointments().size() - 1)
				.isDeleted()) {
				appointments.add(chatroom.getAppointments().get(chatroom.getAppointments().size() - 1));
			}
		}
		List<TodoAppointmentResponse> response = new ArrayList<>();
		for (Appointment appointment : appointments) {
			Chatroom chatroom = appointment.getRoom();
			Board board = boardRepository.findById(chatroom.getBoard().getId())
				.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
			String partner = chatroom.getPerformer().getId().equals(memberId) ? chatroom.getWriter().getName() :
				chatroom.getPerformer().getName();
			response.add(
				new TodoAppointmentResponse(appointment.getId(), board.getStatus(), partner, appointment.getApptTime(),
					board.getTitle(), chatroom.getId()));
		}
		return response;
	}

	@Transactional
	public UUID createAppointment(CreateAppointmentRequest request, UUID memberId) {
		Chatroom chatroom = chatroomRepository.findById(request.getRoomId())
			.orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
		Appointment appointment = new Appointment(chatroom, request.getAppTime(), request.getTimeQnt());
		appointmentRepository.save(appointment);
		// TODO : Board Status 무슨 String으로 설정할지 / 약속 삭제하지 않고 생성하기 호출할 땐 어떻게 해야하는가 / 삭제된 Board에 접근 시 예외 처리?
		boardRepository.findById(chatroom.getBoard().getId())
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND))
			.changeStatus("진행중");
		List<Appointment> appointments = chatroom.getAppointments();

		if (!appointments.isEmpty()) {
			appointments.get(appointments.size() - 1).softDelete();
		}

		appointments.add(appointment);

		// 보증금 받기
		Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		Account account = accountRepository.findByMemberIdAndDeletedAtIsNull(member.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
		if (account.getTimeQnt() < appointment.getTimeQnt())
			throw new CustomException(ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);

		account.setBalance(account.getTimeQnt() - appointment.getTimeQnt());
		DepositLog depositLog = DepositLog.builder()
									.type(DepositType.DEPOSIT)
									.amount(appointment.getTimeQnt())
									.account(account)
									.build();
		depositLogRepository.save(depositLog);
		return appointment.getId();
	}

	@Transactional
	public void deleteAppointment(UUID appointmentId, UUID memberId) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
			.orElseThrow(() -> new CustomException(ErrorCode.APPOINTMENT_NOT_FOUND));
		if (memberId.equals(appointment.getRoom().getWriter().getId())) {
			appointment.softDelete();
			Chatroom chatroom = chatroomRepository.findById(appointment.getRoom().getId())
				.orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
			Board board = boardRepository.findById(chatroom.getBoard().getId())
				.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
			board.changeStatus("진행전");
			// 보증금 받기
			Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
			Account account = accountRepository.findByMemberIdAndDeletedAtIsNull(member.getId())
				.orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
			account.setBalance(account.getTimeQnt() + appointment.getTimeQnt());

			DepositLog depositLog = DepositLog.builder()
					.type(DepositType.REFUND)
					.amount(appointment.getTimeQnt())
					.account(account)
					.build();
			depositLogRepository.save(depositLog);
			// TODO : 존재하는 약속이지만 이미 삭제된 약속이라면?
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

	private void extractAppointmentFromChatroom(List<Chatroom> chatrooms, List<DetailAppointmentResponse> responses) {
		for (Chatroom chatroom : chatrooms) {
			if (!chatroom.getAppointments().isEmpty()) {
				Appointment appointment = chatroom.getAppointments().get(chatroom.getAppointments().size() - 1);
				if (!appointment.isDeleted()) {
					responses.add(new DetailAppointmentResponse(appointment.getId(), appointment.getApptTime(),
						appointment.getTimeQnt(), appointment.getCreatedAt(), chatroom.getBoard().getTitle()));
				}
			}
		}
	}

	public BriefAppointmentResponse getAppointment(UUID roomId, UUID memberId) {
		Optional<Appointment> appointment = appointmentRepository.findByRoomIdAndDeletedAtIsNull(roomId);
		if (appointment.isEmpty())
			return new BriefAppointmentResponse();
		if (!memberId.equals(appointment.get().getRoom().getWriter().getId()) && !memberId.equals(
			appointment.get().getRoom().getPerformer().getId())) {
			throw new CustomException(ErrorCode.APPOINTMENT_NOT_AUTHORIZED);
		} else {
			return new BriefAppointmentResponse(appointment.get().getId(), appointment.get().getApptTime(),
				appointment.get().getTimeQnt());
		}
	}

	@Transactional(readOnly = true)
	public List<TradeLogFindAllResponse> getAppointedBoardsByMemberId(UUID username) {

		List<Appointment> appointments = appointmentRepository.findAppointmentsWithBoardByMemberId(username);

		return appointments.stream()
			.map(appointment -> {
				UUID memberId = username.equals(appointment.getRoom().getWriter().getId()) ?
					appointment.getRoom().getPerformer().getId() :
					appointment.getRoom().getWriter().getId();
				Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId).get();
				Board board = appointment.getRoom().getBoard();
				byte[] partnerImage = fileService.getProfileImage(memberId);  // partnerImage 조회 로직 추가
				return TradeLogFindAllResponse.from(board, member, partnerImage);
			})
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<TradeLogFindAllResponse> searchBoardsByMemberIdAndKeyword(UUID memberId, String keyword) {
		// 1. Board 목록을 검색
		List<Board> boards = appointmentRepository.searchBoardsByMemberIdAndKeyword(memberId, keyword);

		// 2. 검색된 Board를 TradeLogFindAllResponse로 변환
		return boards.stream().map(TradeLogFindAllResponse::from)  // Board -> TradeLogFindAllResponse 변환
			.collect(Collectors.toList());
	}

}
