package com.taesan.tikkle.domain.account;

import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import com.taesan.tikkle.domain.account.entity.ExchangeLog;
import com.taesan.tikkle.domain.account.dto.ExchangeType;
import com.taesan.tikkle.domain.account.repository.AccountRepository;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotRepository;
import com.taesan.tikkle.domain.account.repository.ExchangeRepository;
import com.taesan.tikkle.domain.account.service.SettlementService;
import com.taesan.tikkle.domain.board.repository.BoardRepository;
import com.taesan.tikkle.domain.rate.entity.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SettlementServiceTest {

    @InjectMocks
    private SettlementService settlementService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private BalanceSnapshotRepository balanceSnapshotRepository;

    @Mock
    private BoardRepository boardRepository;

    private UUID accountId;
    private Account account;
    private ExchangeLog exchangeLog;
    private Rate rate;
    private BalanceSnapshot balanceSnapshot;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        account = Account.builder()
                .timeQnt(10)
                .build();
        accountId = account.getId();

        rate = Rate.builder()
                .timeToRank(1000)
                .build();

        exchangeLog = ExchangeLog.builder()
                .account(account)
                .quantity(1)
                .exchangeType(ExchangeType.TTOR)
                .rate(rate)
                .build();

        balanceSnapshot = BalanceSnapshot.builder()
                .accountId(accountId)
                .timeQnt(10)
                .rankingPoint(0)
                .build();
    }

    @Test
    public void should_ReturnTrue_When_AccountBalanceMatches() {
        // Given
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 29, 59);

        List<ExchangeLog> exchangeLogs = Arrays.asList(exchangeLog);
        List<Account> accounts = Arrays.asList(account);

        account.updateAccount(exchangeLog.getExchangeType(),
                exchangeLog.getRate().getTimeToRank(),
                exchangeLog.getQuantity());

        when(exchangeRepository.findByCreatedAtBetween(startOfDay, endOfDay)).thenReturn(exchangeLogs);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(balanceSnapshotRepository
                .findFirstByAccountIdOrderByCreatedAtDesc(account.getId()))
                .thenReturn(Optional.ofNullable(balanceSnapshot));

        // When
        boolean result = settlementService.performDailySettlement();

        // Then: 계좌 잔액이 일치하므로 true를 반환해야 함
        assertTrue(result);
    }

    @Test
    public void should_ReturnFalse_When_AccountBalanceDoesNotMatch() {
        // Given: 예상되는 잔액과 실제 잔액이 다를 때
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 29, 59);

        // mockAccount의 timeQnt를 10에서 0으로 설정했으나, 로그에서 1이 감소되므로 mismatch 발생
        account.updateAccount(exchangeLog.getExchangeType(),
                exchangeLog.getRate().getTimeToRank(),
                10);

        List<ExchangeLog> exchangeLogs = Arrays.asList(exchangeLog);
        List<Account> accounts = Arrays.asList(account);

        when(exchangeRepository.findByCreatedAtBetween(startOfDay, endOfDay)).thenReturn(exchangeLogs);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(balanceSnapshotRepository
                .findFirstByAccountIdOrderByCreatedAtDesc(account.getId()))
                .thenReturn(Optional.ofNullable(balanceSnapshot));

        // When
        boolean result = settlementService.performDailySettlement();

        // Then: 계좌 잔액이 불일치하므로 false를 반환해야 함
        assertFalse(result);
    }

    @Test
    public void should_ReturnTrue_When_NoExchangeLogsExist() {
        // Given: ExchangeLog가 없을 때
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 29, 59);

        List<ExchangeLog> exchangeLogs = Arrays.asList();
        List<Account> accounts = Arrays.asList(account);

        when(exchangeRepository.findByCreatedAtBetween(startOfDay, endOfDay)).thenReturn(exchangeLogs);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(balanceSnapshotRepository
                .findFirstByAccountIdOrderByCreatedAtDesc(account.getId()))
                .thenReturn(Optional.ofNullable(balanceSnapshot));

        // When
        boolean result = settlementService.performDailySettlement();

        // Then: ExchangeLog가 없으면 잔액 비교가 불필요하므로 true 반환
        assertTrue(result);
    }

    @Test
    public void should_HandleMultipleAccountsWithDifferentExchangeLogs() {
        // Given: 여러 계좌와 ExchangeLog가 있을 때
        Account anotherAccount = Account.builder()
                .timeQnt(10)
                .build();

        ExchangeLog anotherExchangeLog = ExchangeLog.builder()
                .account(anotherAccount)
                .quantity(2)
                .exchangeType(ExchangeType.TTOR)
                .rate(rate)
                .build();

        BalanceSnapshot anotherSnapshot = BalanceSnapshot.builder()
                .accountId(anotherAccount.getId())
                .timeQnt(10)
                .rankingPoint(0)
                .build();

        account.updateAccount(exchangeLog.getExchangeType(),
                exchangeLog.getRate().getTimeToRank(),
                exchangeLog.getQuantity());

        anotherAccount.updateAccount(anotherExchangeLog.getExchangeType(),
                anotherExchangeLog.getRate().getTimeToRank(),
                anotherExchangeLog.getQuantity());

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 29, 59);

        List<ExchangeLog> exchangeLogs = Arrays.asList(exchangeLog, anotherExchangeLog);
        List<Account> accounts = Arrays.asList(account, anotherAccount);

        when(exchangeRepository.findByCreatedAtBetween(startOfDay, endOfDay)).thenReturn(exchangeLogs);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(balanceSnapshotRepository
                .findFirstByAccountIdOrderByCreatedAtDesc(account.getId()))
                .thenReturn(Optional.ofNullable(balanceSnapshot));
        when(balanceSnapshotRepository
                .findFirstByAccountIdOrderByCreatedAtDesc(anotherAccount.getId()))
                .thenReturn(Optional.ofNullable(anotherSnapshot));

        // When
        boolean result = settlementService.performDailySettlement();

        // Then: 모든 계좌의 잔액이 일치하면 true를 반환해야 함
        assertTrue(result);
    }
}
