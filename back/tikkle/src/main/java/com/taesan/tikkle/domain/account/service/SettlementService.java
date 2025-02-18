package com.taesan.tikkle.domain.account.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.config.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import com.taesan.tikkle.domain.account.repository.ExchangeRepository;
import com.taesan.tikkle.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

/*
 *  TODO: 랭킹 포인트 검증 로직 추가 필요
 * 
 */
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final AccountRepository accountRepository;
    private final ExchangeRepository exchangeRepository;
    private static final Logger logger = LoggerFactory.getLogger(SettlementService.class);


    @Transactional
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

            int expectedTimeQnt = account.getTimeQnt();  // 예상되는 잔액
            for (ExchangeLog log : accountLogs) {
                if (log.getExchangeType() == ExchangeType.TTOR) {
                    expectedTimeQnt -= log.getQuantity();
                } else if (log.getExchangeType() == ExchangeType.RTOT) {
                    expectedTimeQnt += log.getQuantity();
                }
            }

            if (account.getTimeQnt() != expectedTimeQnt) {
                logger.error("Account balance mismatch for accountId {}: expected (timeQnt={}, ), actual (timeQnt={}, rankingPoint={})",
                        account.getId(), expectedTimeQnt,
                        account.getTimeQnt(), account.getRankingPoint());
                isFlawless = false;
            }
        }

        return isFlawless;
    }
}

