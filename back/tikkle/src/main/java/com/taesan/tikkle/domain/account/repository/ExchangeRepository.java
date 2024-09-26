package com.taesan.tikkle.domain.account.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.account.entity.ExchangeLog;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeLog, UUID> {
}
