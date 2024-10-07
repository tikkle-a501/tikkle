use tikkle;

--  organizations
INSERT INTO organizations (id, created_at, payment_policy, provider, domain_addr, name)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), NOW(), 'FREE', 1, 'j11a501.p.ssafy.io',
        'DEFAULT');

-- member
INSERT INTO members (id, organization_id, name, email, nickname, created_at)
VALUES (UNHEX(REPLACE(UUID(), '-', '')),
        (SELECT id FROM organizations WHERE name = 'DEFAULT'),
        '허동민',
        'herdongmin@gmail.com',
        '허동크', now()),
       (UNHEX(REPLACE(UUID(), '-', '')),
        (SELECT id FROM organizations WHERE name = 'DEFAULT'),
        '김성윤',
        'atom@gmail.com',
        '김프로', now());

-- accounts
INSERT INTO accounts (id, member_id, time_qnt, ranking_point)
VALUES (UNHEX(REPLACE(UUID(), '-', '')),
        (SELECT id FROM members WHERE email = 'herdongmin@gmail.com'), 10, 0),
       (UNHEX(REPLACE(UUID(), '-', '')),
        (SELECT id FROM members WHERE email = 'atom@gmail.com'), 10, 0);

-- rates
DROP PROCEDURE IF EXISTS InsertRateData;

DELIMITER //

CREATE PROCEDURE InsertRateData()
BEGIN
    DECLARE current_datetime DATETIME DEFAULT '2024-10-01 00:00:00';
    DECLARE end_datetime DATETIME DEFAULT DATE_SUB(NOW(), INTERVAL MINUTE(NOW()) MINUTE);

    DECLARE initial_time_to_rank INT DEFAULT 1000;
    DECLARE time_to_rank INT;
    DECLARE change_percentage FLOAT;

    WHILE current_datetime <= end_datetime
        DO
            -- -3% ~ +3% 범위의 랜덤 변동 값을 생성
            SET change_percentage = (RAND() * 0.06) - 0.03;
            SET time_to_rank = ROUND(initial_time_to_rank * (1 + change_percentage));

            -- 데이터 삽입
            INSERT INTO rates (id, created_at, time_to_rank)
            VALUES (UNHEX(REPLACE(UUID(), '-', '')), current_datetime, time_to_rank);

            -- 다음 시간으로 이동
            SET initial_time_to_rank = time_to_rank;
            SET current_datetime = DATE_ADD(current_datetime, INTERVAL 1 HOUR);
        END WHILE;
END//

DELIMITER ;

CALL InsertRateData();

-- 환전 내역
DROP PROCEDURE IF EXISTS InsertExchangelog;

DELIMITER //

CREATE PROCEDURE InsertExchangelog()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE quantity INT;
    DECLARE account_id BINARY(16);
    DECLARE rates_id BINARY(16);
    DECLARE exchange_type ENUM ('TTOR', 'RTOT');
    DECLARE random_time DATETIME;
    DECLARE closest_rate_id BINARY(16);

    -- 이메일을 기준으로 계좌 ID를 가져오기
    SELECT a.id
    INTO account_id
    FROM accounts a
             JOIN members m ON a.member_id = m.id
    WHERE m.email = 'herdongmin@gmail.com'
    LIMIT 1;

    WHILE i < 500
        DO
            -- 랜덤으로 거래 유형과 수량을 설정
            SET exchange_type = IF(RAND() < 0.5, 'TTOR', 'RTOT');
            SET quantity = FLOOR(RAND() * 10) + 1;
            -- 1부터 10 사이의 랜덤 수량

            -- 랜덤으로 시간 설정 (0분~5000분 전까지, 대략 3.5일 이전까지)
            SET random_time = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 5000) MINUTE);

            -- 해당 시간에 가장 가까운 직전 정각에 있는 rates의 id를 가져옴
            SELECT id
            INTO closest_rate_id
            FROM rates
            WHERE created_at <= random_time
            ORDER BY created_at DESC
            LIMIT 1;

            -- 데이터 삽입
            INSERT INTO exchange_logs (id, created_at, type, quantity, account_id, rates_id)
            VALUES (UNHEX(REPLACE(UUID(), '-', '')),
                    random_time,
                    exchange_type,
                    quantity,
                    account_id,
                    closest_rate_id);

            SET i = i + 1;
        END WHILE;
END//

DELIMITER ;

call InsertExchangelog()