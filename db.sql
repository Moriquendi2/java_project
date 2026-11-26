-- Team 7: Hao Yang, Zexuan Li, Haolin Chen.
-- Minimal schema used by the standalone JDBC connection demo.
-- 中文：本脚本为独立 JDBC 演示创建最小数据库结构和停车分区样例数据。

-- Create and select the database used by the example.
-- 中文：创建示例数据库并将后续 SQL 操作切换到该数据库。
CREATE DATABASE IF NOT EXISTS mia_parking
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE mia_parking;

-- Store the identifier, display name, short description, and sample capacity of each zone.
-- 中文：创建停车分区表，保存分区编号、名称、说明和样例容量。
CREATE TABLE IF NOT EXISTS PARKING_ZONE (
    zone_id VARCHAR(20) NOT NULL,
    zone_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL DEFAULT '',
    capacity INT UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (zone_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed three repeatable sample rows so the JDBC demo has data to display.
-- 中文：插入三个可重复执行的分区样例，供 JDBC 演示读取。
INSERT INTO PARKING_ZONE (zone_id, zone_name, description, capacity)
VALUES
    ('A', 'Zone A', 'Near Terminal A', 4),
    ('B', 'Zone B', 'Near Terminal B', 4),
    ('C', 'Zone C', 'Long-stay and remote parking', 4)
ON DUPLICATE KEY UPDATE
    zone_name = VALUES(zone_name),
    description = VALUES(description),
    capacity = VALUES(capacity);
