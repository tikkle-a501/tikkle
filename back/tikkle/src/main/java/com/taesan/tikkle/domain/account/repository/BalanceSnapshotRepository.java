package com.taesan.tikkle.domain.account.repository;

import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BalanceSnapshotRepository extends JpaRepository<BalanceSnapshot, UUID> {
}
