package com.taesan.tikkle.domain.account.service.strategy;

public enum SnapshotStrategyType {
    JPA("JPA findAll() saveAll() with Batch", "jpaSnapshotStrategy"),
    STREAM("STREAM API with Batch", "streamSnapshotStrategy"),
    PAGING("PAGING with Batch", "pagingSnapshotStrategy"),
    JDBC("JDBC SELECT INTO", "jdbcSnapshotStrategy");

    private final String description;
    private final String beanName;

    SnapshotStrategyType(String description, String beanName) {
        this.description = description;
        this.beanName = beanName;
    }

    public String getDescription() {
        return description;
    }

    public String getBeanName() {
        return beanName;
    }
}
