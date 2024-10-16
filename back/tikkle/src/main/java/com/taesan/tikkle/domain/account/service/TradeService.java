package com.taesan.tikkle.domain.account.service;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.repository.TradeLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeService {

	private final TradeLogRepository tradeLogRepository;

}
