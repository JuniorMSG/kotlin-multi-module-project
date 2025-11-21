-- user.sql
-- DB 연결 테스트용 간단한 샘플 데이터 (2개)

INSERT INTO users (email, password, name, is_active, role, failed_login_attempts, is_locked, created_at, updated_at)
VALUES
    ('test1@test.com', 'password123', '테스트1', true, 'USER', 0, false, NOW(), NOW()),
    ('test2@test.com', 'password456', '테스트2', true, 'ADMIN', 0, false, NOW(), NOW());
