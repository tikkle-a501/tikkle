package com.taesan.tikkle.domain.chat.repository;

import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.chat.entity.Chat;

@Repository
public interface ChatRepository extends MongoRepository<Chat, ObjectId> {
	// roomId에 해당하는 최신 Chat을 timestamp 기준으로 가져오기
	Chat findTopByChatroomIdOrderByTimestampDesc(String chatroomId);

	// ChatRoom id에 따른 Chat 반환
	List<Chat> findByChatroomIdOrderByTimestampAsc(String roomId);

	Chat findByChatroomId(String chatroomId);
}
