-- ✅ hexagonal_payment 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS hexagonal_payment
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- ✅ hexagonal_payment 전용 사용자 생성
CREATE USER IF NOT EXISTS 'payment_user'@'%' IDENTIFIED BY 'payment123';

-- ✅ 권한 부여
GRANT ALL PRIVILEGES ON hexagonal_payment.* TO 'payment_user'@'%';
FLUSH PRIVILEGES;
