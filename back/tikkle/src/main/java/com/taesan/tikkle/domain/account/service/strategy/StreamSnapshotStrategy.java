package com.taesan.tikkle.domain.account.service.strategy;

import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import com.taesan.tikkle.domain.account.repository.AccountCustomRepository;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Component("streamSnapshotStrategy")
@RequiredArgsConstructor
public class StreamSnapshotStrategy implements SnapshotStrategy {

    private final AccountCustomRepository accountCustomRepository;
    private final BalanceSnapshotRepository balanceSnapshotRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(StreamSnapshotStrategy.class);

    @Override
    public void createSnapShots() {
        logger.info("Creating snapshots using Stream API strategy");

        int fetchSize = 1000;
        int processedCount = 0;

        try (Stream<Account> accountStream = accountCustomRepository.findAllWithStream(fetchSize)) {
            List<BalanceSnapshot> batchSnapshots = new ArrayList<>(fetchSize);
            Iterator<Account> iterator = accountStream.iterator();

            while (iterator.hasNext()) {
                Account account = iterator.next();
                BalanceSnapshot snapshot = convertToSnapshot(account);
                batchSnapshots.add(snapshot);

                if (batchSnapshots.size() >= fetchSize) {
                    processedCount += batchSnapshots.size();
                    balanceSnapshotRepository.saveAll(batchSnapshots);
                    entityManager.flush();
                    entityManager.clear();
                    batchSnapshots.clear();
                }
            }

            if (!batchSnapshots.isEmpty()) {
                processedCount += batchSnapshots.size();
                balanceSnapshotRepository.saveAll(batchSnapshots);
                entityManager.flush();
                entityManager.clear();
            }
        }

        logger.info("Created {} snapshots using Stream API strategy", processedCount);
    }

    @Override
    public String getStrategyName() {
        return SnapshotStrategyType.STREAM.getDescription();
    }

    private BalanceSnapshot convertToSnapshot(Account account) {
        return BalanceSnapshot.builder()
                .accountId(account.getId())
                .timeQnt(account.getTimeQnt())
                .rankingPoint(account.getRankingPoint())
                .build();
    }
}
