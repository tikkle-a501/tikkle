package com.taesan.tikkle.domain.account.service;

public interface SnapshotStrategy {
    void createSnapShots();
    String getStrategyName();
}
