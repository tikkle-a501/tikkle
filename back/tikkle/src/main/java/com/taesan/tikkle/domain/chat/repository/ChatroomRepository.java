package com.taesan.tikkle.domain.chat.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.chat.entity.Chatroom;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {
	Optional<Chatroom> findByBoardId(UUID boardId);
}
