-- user.sql
-- DB 연결 테스트용 간단한 샘플 데이터 (2개)

INSERT INTO users (email, name, purchase_amount, grade, marked_for_update) VALUES
                                                                                           ('bronze1@ms.com', 'Bronze User 1', 500000, 'BRONZE', false),
                                                                                           ('bronze2@ms.com', 'Bronze User 2', 800000, 'BRONZE', false),
                                                                                           ('silver1@ms.com', 'Silver User 1', 1500000, 'BRONZE', false),
                                                                                           ('silver2@ms.com', 'Silver User 2', 3000000, 'SILVER', false),
                                                                                           ('gold1@ms.com', 'Gold User 1', 6000000, 'SILVER', false),
                                                                                           ('gold2@ms.com', 'Gold User 2', 8000000, 'GOLD', false),
                                                                                           ('vip1@ms.com', 'VIP User 1', 12000000, 'GOLD', false),
                                                                                           ('vip2@ms.com', 'VIP User 2', 15000000, 'VIP', false);
