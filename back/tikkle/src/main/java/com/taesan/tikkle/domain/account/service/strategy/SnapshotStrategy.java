package com.taesan.tikkle.domain.account.service.strategy;

public interface SnapshotStrategy {
    void createSnapShots();
    String getStrategyName();
}
