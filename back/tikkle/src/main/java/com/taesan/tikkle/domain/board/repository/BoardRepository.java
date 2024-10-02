package com.taesan.tikkle.domain.board.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
	//    Optional<Board> findByCategory(String cate);
	List<Board> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

	// 제목에 keyword가 포함된 게시물 검색
	List<Board> findByTitleContaining(String keyword);

	// 내용에 keyword가 포함된 게시물 검색
	List<Board> findByContentContaining(String keyword);
}


