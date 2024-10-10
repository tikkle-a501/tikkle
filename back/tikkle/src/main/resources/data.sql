use tikkle;
SET time_zone = '+09:00';
SELECT @@global.time_zone, @@session.time_zone;
DROP PROCEDURE IF EXISTS create_members_with_accounts;
DROP PROCEDURE IF EXISTS InsertRateData;
DROP PROCEDURE IF EXISTS InsertExchangeLogData;
--  organizations
INSERT INTO organizations (id, created_at, payment_policy, provider, domain_addr, name)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), NOW(), 'FREE', 1, 'j11a501.p.ssafy.io',
        'DEFAULT');

-- member and accounts
DELIMITER //

CREATE PROCEDURE create_members_with_accounts()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE org_id BINARY(16);
    DECLARE member_id BINARY(16);
    DECLARE random_time_qnt INT;
    DECLARE random_ranking_point INT;

    -- DEFAULT organization의 id를 가져옵니다.
    SELECT id INTO org_id FROM organizations WHERE name = 'DEFAULT';

    WHILE i < 100
        DO
            -- 새 멤버 ID 생성
            SET member_id = UNHEX(REPLACE(UUID(), '-', ''));

            -- 랜덤 값 생성
            SET random_time_qnt = FLOOR(RAND() * 101); -- 0에서 100 사이의 랜덤 값 생성
            SET random_ranking_point = FLOOR(RAND() * 10001);
            -- 0에서 10,000 사이의 랜덤 값 생성

            -- members 테이블에 데이터 삽입
            INSERT INTO members (id, organization_id, name, email, nickname, created_at)
            VALUES (member_id,
                    org_id,
                    CONCAT('Member', i),
                    CONCAT('member', i, '@example.com'),
                    CONCAT('Nickname', i),
                    NOW());

            -- accounts 테이블에 해당 멤버의 계정 데이터 삽입
            INSERT INTO accounts (id, member_id, time_qnt, ranking_point)
            VALUES (UNHEX(REPLACE(UUID(), '-', '')),
                    member_id,
                    random_time_qnt,
                    random_ranking_point);

            SET i = i + 1;
        END WHILE;
END//

DELIMITER ;

-- rates
DELIMITER //

CREATE PROCEDURE InsertRateData()
BEGIN
    DECLARE current_datetime DATETIME DEFAULT '2024-01-01 00:00:00';
    DECLARE end_datetime DATETIME DEFAULT DATE_SUB(NOW(), INTERVAL MINUTE(NOW()) MINUTE);

    DECLARE initial_time_to_rank INT DEFAULT 1000;
    DECLARE time_to_rank INT;
    DECLARE change_percentage FLOAT;

    WHILE current_datetime <= end_datetime
        DO
            -- -1% ~ +1% 범위의 랜덤 변동 값을 생성
            SET change_percentage = (RAND() * 0.02) - 0.01;
            -- RAND() * 0.02로 0 ~ 0.02 사이의 값을 만들고, -0.01을 빼서 -0.01 ~ 0.01 사이로 설정

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

-- 환전 내역
DELIMITER //

CREATE PROCEDURE InsertExchangeLogData()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE total_logs INT DEFAULT 100; -- 생성할 로그의 수 (원하는 대로 조정 가능)
    DECLARE rand_account_id BINARY(16);
    DECLARE rand_rates_id BINARY(16);
    DECLARE rand_type VARCHAR(8);
    DECLARE rand_quantity TINYINT UNSIGNED;
    DECLARE rand_datetime DATETIME;
    DECLARE truncated_datetime DATETIME;

    WHILE i < total_logs
        DO
            -- accounts 테이블에서 account_id를 랜덤으로 선택
            SELECT id INTO rand_account_id FROM accounts ORDER BY RAND() LIMIT 1;

            -- 수량을 1에서 25 사이로 랜덤 설정
            SET rand_quantity = FLOOR(RAND() * 25) + 1;

            -- 70% 확률로 현재 시간 기준 1시간 전의 시간을 선택하고, 나머지 30%는 지난 1년 중 랜덤 시간 설정
            IF RAND() <= 0.7 THEN
                SET rand_datetime = DATE_SUB(NOW(), INTERVAL 1 HOUR);
            ELSE
                SET rand_datetime = DATE_ADD(NOW(), INTERVAL -FLOOR(RAND() * 365) DAY);
            END IF;

            -- rand_datetime에서 시간만 남기고 분과 초를 0으로 설정하여 정시로 맞춤
            SET truncated_datetime = DATE_FORMAT(rand_datetime, '%Y-%m-%d %H:00:00');

            -- rates 테이블에서 truncated_datetime에 해당하는 rates_id를 가져옴
            SELECT id
            INTO rand_rates_id
            FROM rates
            WHERE created_at = truncated_datetime
            LIMIT 1;

            -- 거래 타입을 랜덤으로 설정 ('TTOR' 또는 'RTOT')
            SET rand_type = CASE FLOOR(RAND() * 2)
                                WHEN 0 THEN 'TTOR'
                                WHEN 1 THEN 'RTOT'
                END;

            -- exchange_logs 테이블에 데이터 삽입
            INSERT INTO exchange_logs (id, created_at, type, quantity, account_id, rates_id)
            VALUES (UNHEX(REPLACE(UUID(), '-', '')),
                    rand_datetime,
                    rand_type,
                    rand_quantity,
                    rand_account_id,
                    rand_rates_id);

            SET i = i + 1;
        END WHILE;
END//

DELIMITER ;

CALL create_members_with_accounts();
CALL InsertRateData();
call InsertExchangeLogData()