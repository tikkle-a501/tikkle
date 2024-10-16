package com.taesan.tikkle.domain.account.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.account.entity.TradeLog;

@Repository
public interface TradeLogRepository extends JpaRepository<TradeLog, UUID> {

	List<TradeLog> findTradeLogsByMemberId(UUID memberId);

}
