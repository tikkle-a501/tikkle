package com.taesan.tikkle.domain.account.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotRepository;
import com.taesan.tikkle.domain.config.security.CustomUserDetails;
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
    private static final Logger logger = LoggerFactory.getLogger(SettlementService.class);


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

            if (accountLogs == null) {
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

            for (ExchangeLog log : accountLogs) {
                if (log.getExchangeType() == ExchangeType.TTOR) {
                    historicalTQ -= log.getQuantity();
                    historicalRP += log.getQuantity() * log.getRate().getTimeToRank();
                } else if (log.getExchangeType() == ExchangeType.RTOT) {
                    historicalTQ += log.getQuantity();
                    historicalRP -= log.getQuantity() * log.getRate().getTimeToRank();
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

    /*
        TODO: 배치 처리 변환 필요
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void createSnapshots() {
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
}

