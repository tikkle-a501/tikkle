package com.taesan.tikkle.domain.account.repository;

import com.taesan.tikkle.domain.account.entity.Account;

import java.util.stream.Stream;

public interface AccountCustomRepository {
    Stream<Account> findAllWithStream(int fetchSize);
}
