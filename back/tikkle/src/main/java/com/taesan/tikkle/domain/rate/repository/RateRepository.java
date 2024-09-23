package com.taesan.tikkle.domain.rate.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.rate.entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, UUID> {

	Optional<Rate> findTopByOrderByCreatedAtDesc();
}
