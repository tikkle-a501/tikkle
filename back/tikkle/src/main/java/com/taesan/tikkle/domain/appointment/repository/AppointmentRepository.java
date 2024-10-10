package com.taesan.tikkle.domain.appointment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.appointment.entity.Appointment;
import com.taesan.tikkle.domain.board.entity.Board;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
	Optional<Appointment> findByRoomIdAndDeletedAtIsNull(UUID roomId);

	@Query("SELECT a.room.board FROM Appointment a " +
		"WHERE a.room.performer.id = :memberId OR a.room.writer.id = :memberId")
	List<Board> findBoardsByMemberId(@Param("memberId") UUID memberId);

	@Query("SELECT a FROM Appointment a JOIN FETCH a.room c JOIN FETCH c.board " +
		"WHERE (c.writer.id = :memberId OR c.performer.id = :memberId)")
	List<Appointment> findAppointmentsWithBoardByMemberId(@Param("memberId") UUID memberId);

	@Query("SELECT a.room.board FROM Appointment a " +
		"WHERE (a.room.performer.id = :memberId OR a.room.writer.id = :memberId) " +
		"AND (a.room.board.title LIKE %:keyword% OR a.room.board.content LIKE %:keyword%)")
	List<Board> searchBoardsByMemberIdAndKeyword(@Param("memberId") UUID memberId, @Param("keyword") String keyword);

}
