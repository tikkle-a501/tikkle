package com.taesan.tikkle.domain.appointment.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.appointment.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
	Optional<Appointment> findByRoomId(UUID roomId);
}
