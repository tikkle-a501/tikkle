package com.taesan.tikkle.domain.account.service;

import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import com.taesan.tikkle.domain.account.repository.AccountRepository;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("jpaSnapshotStrategy")
@RequiredArgsConstructor
public class JpaSnapshotStrategy implements SnapshotStrategy {

    private final AccountRepository accountRepository;
    private final BalanceSnapshotRepository balanceSnapshotRepository;
    private static final Logger logger = LoggerFactory.getLogger(JpaSnapshotStrategy.class);

    @Override
    public void createSnapShots() {
        List<Account> accounts = accountRepository.findAll();
        List<BalanceSnapshot> snapshots = accounts.stream()
                .map(account -> BalanceSnapshot.builder()
                        .accountId(account.getId())
                        .timeQnt(account.getTimeQnt())
                        .rankingPoint(account.getRankingPoint())
                        .build())
                .toList();

        balanceSnapshotRepository.saveAll(snapshots);
    }

    @Override
    public String getStrategyName() {
        return SnapshotStrategyType.JPA.getDescription();
    }
}
