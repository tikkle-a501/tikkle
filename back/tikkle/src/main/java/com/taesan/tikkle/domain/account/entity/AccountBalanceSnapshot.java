package com.taesan.tikkle.domain.account.entity;

import com.taesan.tikkle.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "account_balance_snapshot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountBalanceSnapshot extends BaseEntity {

    @Id
    private UUID id;

    // 계좌 ID 이외의 정보는 필요하지 않으므로 Id만 이용
    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private int balance;

}
