package com.taesan.tikkle.domain.account.service;

import org.springframework.stereotype.Service;

import com.taesan.tikkle.domain.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	
}
