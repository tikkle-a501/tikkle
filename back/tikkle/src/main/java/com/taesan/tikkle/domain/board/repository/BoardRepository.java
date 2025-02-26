package com.taesan.tikkle.domain.board.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
	//    Optional<Board> findByCategory(String cate);
	List<Board> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

	// 제목에 keyword가 포함되면서 삭제되지 않은 게시물만 검색
	List<Board> findByTitleContainingAndDeletedAtIsNull(String keyword);

	// 내용에 keyword가 포함되면서 삭제되지 않은 게시물만 검색
	List<Board> findByContentContainingAndDeletedAtIsNull(String keyword);
	
	// memberId에 따른 게시글 조회 최신순으로 반환
	List<Board> findByMemberIdAndDeletedAtIsNullOrderByCreatedAtDesc(UUID username);

	@Query("SELECT b FROM Board b " +
			"WHERE b.deletedAt IS NULL AND b.status = :status AND b.member.id = :memberId " +
			"ORDER BY b.createdAt DESC")
	List<Board> findActiveBoardsByMember(@Param("status") String status, @Param("memberId") UUID memberId);

}


