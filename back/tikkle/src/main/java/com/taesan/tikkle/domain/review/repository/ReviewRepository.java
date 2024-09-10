package com.taesan.tikkle.domain.review.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
