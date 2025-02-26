package com.taesan.tikkle.domain.account.entity;

import com.taesan.tikkle.domain.account.dto.DepositType;
import com.taesan.tikkle.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deposit_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepositLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private DepositType type; // DEPOSIT(보증금 차감), REFUND(보증금 반환)

    @Builder
    public DepositLog(Account account, Integer amount, DepositType type) {
        this.account = account;
        this.amount = amount;
        this.type = type;
    }
}

