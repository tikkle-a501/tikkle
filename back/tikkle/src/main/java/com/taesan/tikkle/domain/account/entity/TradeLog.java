package com.taesan.tikkle.domain.account.entity;

import java.util.UUID;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.board.entity.Board;
import com.taesan.tikkle.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trade_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeLog extends BaseEntity {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id = UlidCreator.getMonotonicUlid().toUuid();

	@ManyToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@ManyToOne
	@JoinColumn(name = "rec_account_id", nullable = false)
	private Account recAccount;

	@ManyToOne
	@JoinColumn(name = "req_account_id")
	private Account reqAccount;

	private Integer quantity;

	public static TradeLog from(Board board, Account recAccount, Account reqAccount, Integer quantity) {
		TradeLog tradeLog = new TradeLog();
		tradeLog.board = board;
		tradeLog.recAccount = recAccount;
		tradeLog.reqAccount = reqAccount;
		tradeLog.quantity = quantity;
		return tradeLog;
	}
}

