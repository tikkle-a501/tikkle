package com.taesan.tikkle.domain.account;

import com.github.f4b6a3.ulid.UlidCreator;
import com.taesan.tikkle.domain.account.entity.Account;
import com.taesan.tikkle.domain.account.entity.BalanceSnapshot;
import com.taesan.tikkle.domain.account.repository.AccountRepository;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotCustomRepository;
import com.taesan.tikkle.domain.account.repository.BalanceSnapshotRepository;
import com.taesan.tikkle.domain.account.service.*;
import com.taesan.tikkle.domain.member.entity.Member;
import com.taesan.tikkle.domain.member.entity.Role;
import com.taesan.tikkle.domain.member.repository.MemberRepository;
import com.taesan.tikkle.domain.organization.entity.Organization;
import com.taesan.tikkle.domain.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@DataJpaTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        JpaSnapshotStrategy.class,
        JdbcSnapshotStrategy.class,
        PagingSnapshotStrategy.class,
        StreamSnapshotStrategy.class,
        SnapshotTestConfig.class,
        SettlementService.class,
})
public class SnapshotPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(SnapshotPerformanceTest.class);

    @Container
    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.6")
            .withDatabaseName("snapshot_test")
            .withUsername("test")
            .withPassword("test")
            .withCommand("--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_unicode_ci",
                    "--innodb-buffer-pool-size=256M");

    @DynamicPropertySource
    static void registerMariaDBProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mariaDBContainer.getJdbcUrl() + "?rewriteBatchedStatements=true");

        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mariaDBContainer::getDriverClassName);

        // JPA 설정 추가
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.show-sql", () -> "false");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.generate_statistics", () -> "true");
        registry.add("logging.level.org.hibernate.SQL", () -> "DEBUG");
        registry.add("logging.level.org.hibernate.type.descriptor.sql.BasicBinder", () -> "TRACE");
        registry.add("logging.level.org.hibernate.jdbc.batch", () -> "TRACE");


        registry.add("spring.jpa.properties.hibernate.jdbc.batch_size", () -> "100");
        registry.add("spring.jpa.properties.hibernate.order_inserts", () -> "true");
        registry.add("spring.jpa.properties.hibernate.order_updates", () -> "true");
        registry.add("spring.jpa.properties.hibernate.jdbc.batch_versioned_data", () -> "true");
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private BalanceSnapshotRepository balanceSnapshotRepository;

    @Autowired
    private BalanceSnapshotCustomRepository balanceSnapshotCustomRepository;

    // Bean으로 올라간 snapshotStrategy에 대한 Map 주입
    @Autowired
    private Map<String, SnapshotStrategy> snapshotStrategyMap;

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TEST_DATA_SIZE = 50000; // 5만 개의 계정으로 테스트
    private static boolean dataInitialized = false;

    @BeforeAll
    static void setUp() {
        logger.info("Starting MariaDB container for performance tests");
    }

    @BeforeEach
    void setUpEach() {
        if (!dataInitialized) {
            logger.info("Initializing test data...");

            // 테스트 조직 생성
            Organization testOrg = Organization.builder()
                    .name("TEST_ORG")
                    .domainAddr("test.tikkle.com")
                    .provider(0)
                    .paymentPolicy("test")
                    .build();
            organizationRepository.save(testOrg);

            // 테스트 데이터 생성
            generateTestData();

            dataInitialized = true;
            logger.info("Test data initialization completed. {} accounts created.", TEST_DATA_SIZE);
        }

        // 각 테스트 전에 스냅샷 테이블 비우기
        balanceSnapshotRepository.deleteAllInBatch();
        logger.info("Cleared balance snapshot table for next test");
    }

    @AfterAll
    static void tearDown() {
        logger.info("Performance tests completed. Shutting down MariaDB container");
    }

    // 테스트 데이터 생성 시에는 JDBC 배치 이용함
    private void generateTestData() {
        logger.info("Generating {} test accounts using JdbcClient...", TEST_DATA_SIZE);

        // 테스트 조직 가져오기
        Organization org = organizationRepository.findAll().get(0);
        UUID orgId = org.getId();

        // 배치 크기 설정
        int batchSize = 5000;
        int count = TEST_DATA_SIZE;

        try {
            for (int i = 0; i < count; i += batchSize) {
                int currentBatchSize = Math.min(batchSize, count - i);
                long startTime = System.nanoTime();

                // 1. 회원 일괄 삽입 준비
                final String memberSql = "INSERT INTO members (id, email, name, organization_id, role, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
                List<UUID> memberIds = new ArrayList<>(currentBatchSize);

                int finalI1 = i;
                int[] memberResults = jdbcTemplate.batchUpdate(memberSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int j) throws SQLException {
                        int uniqueId = finalI1 + j;
                        UUID memberId = UlidCreator.getMonotonicUlid().toUuid();
                        memberIds.add(memberId);

                        ps.setObject(1, uuidToBytes(memberId));
                        ps.setString(2, "test" + uniqueId + "@example.com");
                        ps.setString(3, "Test User " + uniqueId);
                        ps.setObject(4, uuidToBytes(orgId));
                        ps.setString(5, "ROLE_USER");
                    }

                    @Override
                    public int getBatchSize() {
                        return currentBatchSize;
                    }
                });

                logger.debug("Inserted {} members", Arrays.stream(memberResults).sum());

                // 2. 계정 일괄 삽입 준비
                final String accountSql = "INSERT INTO accounts (id, member_id, time_qnt, ranking_point, created_at) VALUES (?, ?, ?, ?, NOW())";

                int finalI = i;
                int[] accountResults = jdbcTemplate.batchUpdate(accountSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int j) throws SQLException {
                        UUID accountId = UlidCreator.getMonotonicUlid().toUuid();
                        int timeQnt = (finalI + j) % 1000;
                        int rankingPoint = (finalI + j) % 500;

                        ps.setObject(1, uuidToBytes(accountId));
                        ps.setObject(2, uuidToBytes(memberIds.get(j)));
                        ps.setInt(3, timeQnt);
                        ps.setInt(4, rankingPoint);
                    }

                    @Override
                    public int getBatchSize() {
                        return currentBatchSize;
                    }
                });

                long endTime = System.nanoTime();
                logger.info("Generated batch {} to {} in {} ms",
                        i, i + currentBatchSize - 1, TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
            }

            // 저장된 계정 수 확인
            Long accountCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM accounts", Long.class);

            logger.info("Account generation completed. Total accounts: {}", accountCount);

            if (accountCount == 0) {
                logger.error("No accounts were saved! Check database constraints and table names.");
            }

        } catch (Exception e) {
            logger.error("Error generating test data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate test data", e);
        }
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Order(1)
    public void testJpaStrategy() {
        logger.info("Testing JPA strategy...");
        SnapshotStrategy strategy = snapshotStrategyMap.get(SnapshotStrategyType.JPA.getBeanName());
        testStrategy(strategy, SnapshotStrategyType.JPA);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Order(2)
    public void testStreamStrategy() {
        logger.info("Testing Stream API strategy...");
        SnapshotStrategy strategy = snapshotStrategyMap.get(SnapshotStrategyType.STREAM.getBeanName());
        testStrategy(strategy, SnapshotStrategyType.STREAM);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Order(3)
    public void testPagingStrategy() {
        logger.info("Testing Paging strategy...");
        SnapshotStrategy strategy = snapshotStrategyMap.get(SnapshotStrategyType.PAGING.getBeanName());
        testStrategy(strategy, SnapshotStrategyType.PAGING);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Order(4)
    public void testJdbcStrategy() {
        logger.info("Testing JDBC strategy...");
        SnapshotStrategy strategy = snapshotStrategyMap.get(SnapshotStrategyType.JDBC.getBeanName());
        if (strategy == null) {
            logger.warn("JDBC strategy not implemented yet. Skipping test.");
            return;
        }
        testStrategy(strategy, SnapshotStrategyType.JDBC);
    }

    private void testStrategy(SnapshotStrategy strategy, SnapshotStrategyType strategyType) {
        if (strategy == null) {
            logger.warn("Strategy {} not implemented. Skipping test.", strategyType.getDescription());
            return;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 메모리 및 시간 측정
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();

        // 전략 실행
        settlementService.setSnapshotStrategy(strategyType, strategy);
        settlementService.createSnapshots();

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // 결과 계산
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        long memoryUsed = (endMemory - startMemory) / (1024 * 1024); // MB 단위
        long snapshotCount = balanceSnapshotRepository.count();

        logger.info("Strategy: {}, Execution time: {} ms, Memory used: {} MB, Snapshots created: {}",
                strategyType.getDescription(), duration, memoryUsed, snapshotCount);

        Assertions.assertEquals(TEST_DATA_SIZE, snapshotCount,
                "Expected " + TEST_DATA_SIZE + " snapshots, but found " + snapshotCount);
    }
    /**
     * UUID를 BINARY(16) 형식으로 변환
     */
    private byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    /**
     * BINARY(16)를 UUID로 변환
     */
    private UUID bytesToUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }
}