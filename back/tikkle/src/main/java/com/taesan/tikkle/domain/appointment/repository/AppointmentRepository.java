package com.taesan.tikkle.domain.appointment.repository;

import com.taesan.tikkle.domain.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Optional<Appointment> findByRoomIdAndDeletedAtIsNull(UUID roomId);
}
