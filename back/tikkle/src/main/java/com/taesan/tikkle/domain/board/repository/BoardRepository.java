package com.taesan.tikkle.domain.board.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
}
