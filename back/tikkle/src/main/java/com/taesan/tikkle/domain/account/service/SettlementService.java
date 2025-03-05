package com.taesan.tikkle.domain.account.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotRepository;
import com.taesan.tikkle.domain.account.repository.DepositLogRepository;
import com.taesan.tikkle.domain.account.service.strategy.SnapshotStrategy;
import com.taesan.tikkle.domain.account.service.strategy.SnapshotStrategyType;
import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import com.taesan.tikkle.domain.account.repository.ExchangeRepository;
import com.taesan.tikkle.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

/*
 *  TODO: 정산 후 BalanceSnapshot 엔티티 생성 메서드 필요
 *
 */
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final AccountRepository accountRepository;
    private final ExchangeRepository exchangeRepository;
    private final BalanceSnapshotRepository balanceSnapshotRepository;
    private final DepositLogRepository depositLogRepository;
    private final BoardRepository boardRepository;

    private static final Logger logger = LoggerFactory.getLogger(SettlementService.class);

    private final Map<String, SnapshotStrategy> snapshotStrategyMap;
    private SnapshotStrategyType snapshotStrategyType;
    private SnapshotStrategy snapshotStrategy;

    @PostConstruct
    public void init() {
        // 성능 테스트 이후 어플리케이션 메모리 사용 거의 없으며, 수행 시간 낮은 JDBC 전략 이용
        snapshotStrategyType = SnapshotStrategyType.JDBC;
        snapshotStrategy = snapshotStrategyMap.get(snapshotStrategyType.getBeanName());

        if (snapshotStrategy == null) {
            throw new IllegalStateException("Default strategy (JDBC) not found!");
        }

        logger.info("Initialized with default strategy: {} ({})",
                snapshotStrategyType.name(), snapshotStrategyType.getDescription());
    }

    public void setSnapshotStrategy(SnapshotStrategyType strategyType, SnapshotStrategy strategy) {
        this.snapshotStrategyType = strategyType;
        this.snapshotStrategy = strategy;
        logger.info("SnapshotStrategyType: {} ({})",
                strategyType.name(), strategyType.getDescription());
    }

    /*
        TODO: 배치 처리 변환 필요
     */
    @Transactional
    @Scheduled(cron = "0 30 23 * * *")
    public boolean performDailySettlement() {
        boolean isFlawless = true;

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 29, 59);

        List<ExchangeLog> exchangeLogs = exchangeRepository.findByCreatedAtBetween(startOfDay, endOfDay);
        List<Account> accounts = accountRepository.findAll();

        Map<UUID, List<ExchangeLog>> accountLogsMap = exchangeLogs.stream()
                .collect(Collectors.groupingBy(log -> log.getAccount().getId()));

        for (Account account : accounts) {
            List<ExchangeLog> accountLogs = accountLogsMap.get(account.getId());
            List<Board> activeBoards = boardRepository.findActiveBoardsByMember("진행중", account.getId());

            if (accountLogs == null && activeBoards == null) {
                continue;
            }

            Optional<BalanceSnapshot> optSnapshot = balanceSnapshotRepository
                    .findFirstByAccountIdOrderByCreatedAtDesc(account.getId());

            // 해당 계좌 스냅샷이 없더라도 예외를 던지면 안됨
            if (optSnapshot.isEmpty()) {
                logger.error("No BalanceSnapshot found for accountId {}", account.getId());
                continue;
            }

            BalanceSnapshot snapshot = optSnapshot.get();

            int historicalTQ = snapshot.getTimeQnt();
            int historicalRP = snapshot.getRankingPoint();

            if (accountLogs != null) {
                for (ExchangeLog log : accountLogs) {
                    if (log.getExchangeType() == ExchangeType.TTOR) {
                        historicalTQ -= log.getQuantity();
                        historicalRP += log.getQuantity() * log.getRate().getTimeToRank();
                    } else if (log.getExchangeType() == ExchangeType.RTOT) {
                        historicalTQ += log.getQuantity();
                        historicalRP -= log.getQuantity() * log.getRate().getTimeToRank();
                    }
                }
            }

            if (activeBoards != null) {
                // 진행 중인 게시글은 모두 보증금 취급
                for (Board board: activeBoards) {
                    historicalTQ += board.getTime();
                }
            }

            if (account.getTimeQnt() != historicalTQ) {
                logger.error("Account balance mismatch for accountId {}: expected (timeQnt={}, ), actual (timeQnt={}, rankingPoint={})",
                        account.getId(), historicalTQ,
                        account.getTimeQnt(), account.getRankingPoint());
                isFlawless = false;
            }

            if (account.getRankingPoint() != historicalRP) {
                logger.error("Account balance mismatch for accountId {}: expected (RankingPoint={}, ), actual (timeQnt={}, rankingPoint={})",
                        account.getId(), historicalRP,
                        account.getTimeQnt(), account.getRankingPoint());
                isFlawless = false;
            }
        }


        return isFlawless;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void createSnapshots() {
        logger.info("Creating snapshots with strategy: {} ({})",
                snapshotStrategyType.name(), snapshotStrategyType.getDescription());

        long startTime = System.nanoTime();
        snapshotStrategy.createSnapShots();
        long endTime = System.nanoTime();

        logger.info("Completed snapshot creation in {} ms", TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    }
}

