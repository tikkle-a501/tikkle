package com.taesan.tikkle.domain.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.chat.entity.Chatroom;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {
	Optional<Chatroom> findByBoardId(UUID boardId);

	List<Chatroom> findByWriterId(UUID memberId);

	List<Chatroom> findByPerformerId(UUID memberId);

	Optional<Object> findByBoardIdAndWriterIdAndPerformerId(UUID boardId, UUID id, UUID performerId);
}
