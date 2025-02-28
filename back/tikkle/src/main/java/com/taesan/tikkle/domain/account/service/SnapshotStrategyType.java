package com.taesan.tikkle.domain.account.service;

public enum SnapshotStrategyType {
    JPA("JPA findAll()", "jpaSnapshotStrategy"),
    STREAM("STREAM API", "streamSnapshotStrategy"),
    PAGING("PAGING", "pagingSnapshotStrategy"),
    JDBC("JDBC BULK INSERT", "jdbcSnapshotStrategy");

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
