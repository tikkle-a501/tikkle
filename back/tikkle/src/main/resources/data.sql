use tikkle;
SET time_zone = '+09:00';
SELECT @@global.time_zone, @@session.time_zone;
DROP PROCEDURE IF EXISTS create_members_with_accounts;
DROP PROCEDURE IF EXISTS InsertRateData;
DROP PROCEDURE IF EXISTS InsertExchangeLogData;
DROP PROCEDURE IF EXISTS InsertBoardData;
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

-- 공고 생성
DELIMITER //

CREATE PROCEDURE InsertBoardData()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE total_rows INT DEFAULT 100; -- 원하는 만큼의 데이터를 생성하려면 이 값을 조정하세요
    DECLARE rand_member_id BINARY(16);
    DECLARE rand_category VARCHAR(8);
    DECLARE rand_content TEXT;
    DECLARE rand_status VARCHAR(8);
    DECLARE rand_time TINYINT UNSIGNED;
    DECLARE rand_title VARCHAR(64);
    DECLARE rand_view_count INT;
    DECLARE rand_created_at DATETIME(6);
    DECLARE rand_updated_at DATETIME(6);
    DECLARE rand_deleted_at DATETIME(6);

    WHILE i < total_rows
        DO
            -- members 테이블에서 member_id를 랜덤으로 선택
            SELECT id INTO rand_member_id FROM members ORDER BY RAND() LIMIT 1;

            -- 랜덤한 카테고리 설정 ('업무', '비업무')
            SET rand_category = CASE FLOOR(RAND() * 2)
                                    WHEN 0 THEN '업무'
                                    WHEN 1 THEN '비업무'
                END;

            -- 랜덤한 상태 설정 ('진행중', '진행전', '완료됨')
            SET rand_status = CASE FLOOR(RAND() * 3)
                                  WHEN 0 THEN '진행중'
                                  WHEN 1 THEN '진행전'
                                  WHEN 2 THEN '완료됨'
                END;

            -- 랜덤 시간 설정 (1에서 10 사이)
            SET rand_time = FLOOR(RAND() * 10) + 1;

            -- 다양한 타이틀 설정
            SET rand_title = CASE FLOOR(RAND() * 10)
                                 WHEN 0 THEN '내일 회의 자료 출력 및 준비해주실 분!'
                                 WHEN 1 THEN '간단한 회사 문서 복사 업무 도와주실 분!'
                                 WHEN 2 THEN '팀 회의 준비 및 장비 세팅 도와주실 분 구합니다.'
                                 WHEN 3 THEN '업무용 장비 청소 및 정리해주실 분을 찾고 있습니다.'
                                 WHEN 4 THEN '사내 교육 자료 정리 및 프린트 도와주실 분!'
                                 WHEN 5 THEN '사무실 인테리어 변경 도와주실 분 구해요.'
                                 WHEN 6 THEN '문서 파일링 작업 도와줄 분을 찾습니다.'
                                 WHEN 7 THEN '다음 주 이벤트 준비를 도와주실 분을 구합니다.'
                                 WHEN 8 THEN '사무용 물품 정리 및 재고 확인할 분 구합니다.'
                                 WHEN 9 THEN '사무실 내 비품 정리 및 관리 도와주실 분!'
                END;

            -- 다양한 게시글 내용 설정
            SET rand_content = CASE FLOOR(RAND() * 10)
                                   WHEN 0 THEN '내일 오전에 [회의 자료 출력 및 간단한 준비]를 도와주실 분을 찾고 있습니다. 1시간 정도 소요되며.'
                                   WHEN 1 THEN '회사에서 내일 [간단한 문서 복사 및 정리]를 도와주실 분을 구하고 있습니다. 30분 정도 소요되며.'
                                   WHEN 2 THEN '[팀 회의 준비 및 장비 세팅]을 도와줄 분을 찾고 있습니다. 2시간 정도 소요될 예정입니다.'
                                   WHEN 3 THEN '업무용 장비 청소와 정리 작업을 진행할 예정입니다. 약 1시간 30분 정도 소요되며.'
                                   WHEN 4 THEN '사내 교육 자료를 프린트하고 정리할 분을 찾습니다. 1시간 정도 걸릴 예정입니다.'
                                   WHEN 5 THEN '[사무실 인테리어 변경 작업]을 도와주실 분을 구합니다. 시간은 2시간 정도입니다.'
                                   WHEN 6 THEN '다음 주에 필요한 [문서 파일링 작업]을 도와주실 분을 찾습니다. 45분 정도 소요됩니다.'
                                   WHEN 7 THEN '[이벤트 준비]를 도와줄 분을 찾고 있습니다. 1시간 반 정도 걸릴 예정입니다.'
                                   WHEN 8 THEN '[사무용 물품 정리 및 재고 확인] 작업을 도와주실 분을 구합니다. 1시간 소요됩니다.'
                                   WHEN 9 THEN '사무실 내 [비품 정리 및 관리]를 도와주실 분을 찾습니다. 약 2시간 소요됩니다.'
                END;

            -- 랜덤한 조회수 설정 (0에서 1000 사이)
            SET rand_view_count = FLOOR(RAND() * 1000);

            -- 랜덤한 생성 시간 (지난 한 달 동안의 임의의 시간)
            SET rand_created_at = DATE_ADD(NOW(), INTERVAL -FLOOR(RAND() * 30) DAY);

            -- rand_created_at 이후의 임의의 업데이트 시간
            SET rand_updated_at = DATE_ADD(rand_created_at, INTERVAL FLOOR(RAND() * 100) SECOND);

            -- 30% 확률로 삭제된 시간 설정, 나머지는 NULL
            IF RAND() <= 0.3 THEN
                SET rand_deleted_at = DATE_ADD(rand_updated_at, INTERVAL FLOOR(RAND() * 50) SECOND);
            ELSE
                SET rand_deleted_at = NULL;
            END IF;

            -- boards 테이블에 데이터 삽입
            INSERT INTO boards (id, created_at, deleted_at, updated_at, category, content, status, time, title,
                                view_count, member_id)
            VALUES (UNHEX(REPLACE(UUID(), '-', '')),
                    rand_created_at,
                    rand_deleted_at,
                    rand_updated_at,
                    rand_category,
                    rand_content,
                    rand_status,
                    rand_time,
                    rand_title,
                    rand_view_count,
                    rand_member_id);

            SET i = i + 1;
        END WHILE;
END//

DELIMITER ;

CALL create_members_with_accounts();
CALL InsertRateData();
CALL InsertExchangeLogData();
CALL InsertBoardData();