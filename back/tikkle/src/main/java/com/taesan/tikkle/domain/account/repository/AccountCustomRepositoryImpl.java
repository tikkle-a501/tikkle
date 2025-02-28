package com.taesan.tikkle.domain.account.repository;

import com.taesan.tikkle.domain.account.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.stream.Stream;

import static org.hibernate.annotations.QueryHints.READ_ONLY;
import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Stream<Account> findAllWithStream(int fetchSize) {
        return entityManager.createQuery("SELECT a FROM Account a", Account.class)
                .setHint(HINT_FETCH_SIZE, fetchSize)
                .setHint(READ_ONLY, true)
                .getResultStream();
    }
}
