package com.taesan.tikkle.domain.account.service;

import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import com.taesan.tikkle.domain.account.repository.AccountRepository;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("pagingSnapshotStrategy")
@RequiredArgsConstructor
public class PagingSnapshotStrategy implements SnapshotStrategy {

    private final AccountRepository accountRepository;
    private final BalanceSnapshotRepository balanceSnapshotRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(PagingSnapshotStrategy.class);

    @Override
    @Transactional
    public void createSnapShots() {

        logger.info("Creating snapshots using Paging strategy");

        int pageSize = 1000;
        int pageNumber = 0;
        int totalProcessed = 0;
        boolean hasMoreData = true;

        while (hasMoreData) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Account> accountPage = accountRepository.findAll(pageable);

            // 스냅샷 생성 및 저장
            List<BalanceSnapshot> batchSnapshots = accountPage.getContent().stream()
                    .map(this::convertToSnapshot)
                    .collect(Collectors.toList());

            balanceSnapshotRepository.saveAll(batchSnapshots);

            entityManager.flush();
            entityManager.clear();

            totalProcessed += batchSnapshots.size();
            logger.debug("Processed page {} with {} accounts", pageNumber, batchSnapshots.size());

            pageNumber++;
            hasMoreData = !accountPage.isLast();
        }

        logger.info("Created {} snapshots using Paging strategy", totalProcessed);
    }

    @Override
    public String getStrategyName() {
        return SnapshotStrategyType.PAGING.getDescription();
    }

    private BalanceSnapshot convertToSnapshot(Account account) {
        return BalanceSnapshot.builder()
                .accountId(account.getId())
                .timeQnt(account.getTimeQnt())
                .rankingPoint(account.getRankingPoint())
                .build();
    }
}
