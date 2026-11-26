-- Team 7: Hao Yang, Zexuan Li, Haolin Chen.
-- MySQL schema and repeatable initial data for MIA Parking.

-- Create and select the database used by the example.
CREATE DATABASE IF NOT EXISTS mia_parking
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE mia_parking;

-- Site layout and routing distances.
CREATE TABLE IF NOT EXISTS PARKING_ZONE (
    zone_id VARCHAR(20) NOT NULL,
    zone_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL DEFAULT '',
    capacity INT UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (zone_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS TERMINAL (
    terminal_id VARCHAR(20) NOT NULL PRIMARY KEY,
    terminal_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS GATE (
    gate_id VARCHAR(20) NOT NULL PRIMARY KEY,
    gate_type ENUM('ENTRY', 'EXIT', 'BOTH') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS PARKING_SPACE (
    space_id VARCHAR(32) NOT NULL PRIMARY KEY,
    zone_id VARCHAR(20) NOT NULL,
    aisle_code VARCHAR(20) NOT NULL,
    space_number VARCHAR(20) NOT NULL,
    label VARCHAR(100) NOT NULL,
    space_type ENUM('GENERAL', 'MOTORCYCLE', 'EMPLOYEE_RESERVED',
                    'AIRLINE_RESERVED', 'COMMERCIAL', 'PROFESSIONAL_TRANSPORT') NOT NULL,
    long_stay BOOLEAN NOT NULL DEFAULT FALSE,
    `accessible` BOOLEAN NOT NULL DEFAULT FALSE,
    ev_charging BOOLEAN NOT NULL DEFAULT FALSE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uq_space_location (zone_id, aisle_code, space_number),
    KEY idx_space_allocation (zone_id, enabled, space_type, long_stay),
    FOREIGN KEY (zone_id) REFERENCES PARKING_ZONE (zone_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS SPACE_TERMINAL (
    space_id VARCHAR(32) NOT NULL,
    terminal_id VARCHAR(20) NOT NULL,
    walking_distance_m INT UNSIGNED NOT NULL,
    PRIMARY KEY (space_id, terminal_id),
    KEY idx_terminal_distance (terminal_id, walking_distance_m),
    FOREIGN KEY (space_id) REFERENCES PARKING_SPACE (space_id),
    FOREIGN KEY (terminal_id) REFERENCES TERMINAL (terminal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS SPACE_GATE (
    space_id VARCHAR(32) NOT NULL,
    gate_id VARCHAR(20) NOT NULL,
    driving_distance_m INT UNSIGNED NOT NULL,
    PRIMARY KEY (space_id, gate_id),
    FOREIGN KEY (space_id) REFERENCES PARKING_SPACE (space_id),
    FOREIGN KEY (gate_id) REFERENCES GATE (gate_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Flight occurrences, vehicle identification and subscription eligibility.
CREATE TABLE IF NOT EXISTS AIRLINE (
    airline_id VARCHAR(20) NOT NULL PRIMARY KEY,
    airline_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS FLIGHT (
    flight_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(20) NOT NULL,
    terminal_id VARCHAR(20) NOT NULL,
    flight_number VARCHAR(20) NOT NULL,
    direction ENUM('ARRIVAL', 'DEPARTURE') NOT NULL,
    scheduled_at DATETIME(6) NOT NULL,
    UNIQUE KEY uq_flight_occurrence (flight_number, direction, scheduled_at),
    KEY idx_airline_schedule (airline_id, scheduled_at),
    FOREIGN KEY (airline_id) REFERENCES AIRLINE (airline_id),
    FOREIGN KEY (terminal_id) REFERENCES TERMINAL (terminal_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS VEHICLE (
    vehicle_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(32) NOT NULL,
    vehicle_type VARCHAR(32) NOT NULL,
    UNIQUE KEY uq_vehicle_plate (license_plate)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS MEMBERSHIP (
    membership_id VARCHAR(50) NOT NULL PRIMARY KEY,
    customer_category ENUM('SUBSCRIBER', 'AIRPORT_EMPLOYEE', 'AIRLINE_STAFF',
                           'PROFESSIONAL_TRANSPORT') NOT NULL,
    valid_from DATETIME(6) NOT NULL,
    valid_until DATETIME(6) NULL,
    CHECK (valid_until IS NULL OR valid_until > valid_from)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Timestamp columns store UTC. Releasing an assignment leaves its history intact.
CREATE TABLE IF NOT EXISTS PARKING_SESSION (
    session_id VARCHAR(36) NOT NULL PRIMARY KEY,
    vehicle_id BIGINT UNSIGNED NOT NULL,
    membership_id VARCHAR(50) NULL,
    allocated_space_id VARCHAR(32) NOT NULL,
    destination_terminal_id VARCHAR(20) NOT NULL,
    declared_terminal_id VARCHAR(20) NULL,
    declared_airline_id VARCHAR(20) NULL,
    entry_gate_id VARCHAR(20) NOT NULL,
    exit_gate_id VARCHAR(20) NULL,
    customer_category ENUM('OCCASIONAL', 'SUBSCRIBER', 'AIRPORT_EMPLOYEE',
                           'AIRLINE_STAFF', 'PROFESSIONAL_TRANSPORT') NOT NULL,
    assigned_at DATETIME(6) NOT NULL,
    entered_at DATETIME(6) NULL,
    exited_at DATETIME(6) NULL,
    assignment_released_at DATETIME(6) NULL,
    canceled_at DATETIME(6) NULL,
    expected_stay_minutes INT UNSIGNED NOT NULL DEFAULT 1439,
    needs_accessible BOOLEAN NOT NULL DEFAULT FALSE,
    needs_charging BOOLEAN NOT NULL DEFAULT FALSE,
    active_vehicle_id BIGINT UNSIGNED GENERATED ALWAYS AS
        (CASE WHEN exited_at IS NULL AND canceled_at IS NULL THEN vehicle_id END) VIRTUAL,
    active_assignment VARCHAR(32) GENERATED ALWAYS AS
        (CASE WHEN assignment_released_at IS NULL THEN allocated_space_id END) VIRTUAL,
    UNIQUE KEY uq_active_vehicle (active_vehicle_id),
    UNIQUE KEY uq_active_assignment (active_assignment),
    KEY idx_vehicle_visits (vehicle_id, entered_at, session_id),
    KEY idx_terminal_visits (destination_terminal_id, entered_at),
    KEY idx_entry_time (entered_at),
    KEY idx_exit_time (exited_at),
    FOREIGN KEY (vehicle_id) REFERENCES VEHICLE (vehicle_id),
    FOREIGN KEY (membership_id) REFERENCES MEMBERSHIP (membership_id),
    FOREIGN KEY (allocated_space_id) REFERENCES PARKING_SPACE (space_id),
    FOREIGN KEY (destination_terminal_id) REFERENCES TERMINAL (terminal_id),
    FOREIGN KEY (declared_terminal_id) REFERENCES TERMINAL (terminal_id),
    FOREIGN KEY (declared_airline_id) REFERENCES AIRLINE (airline_id),
    FOREIGN KEY (entry_gate_id) REFERENCES GATE (gate_id),
    FOREIGN KEY (exit_gate_id) REFERENCES GATE (gate_id),
    CHECK (expected_stay_minutes > 0),
    CHECK (entered_at IS NULL OR entered_at >= assigned_at),
    CHECK (assignment_released_at IS NULL OR assignment_released_at >= assigned_at),
    CHECK ((exit_gate_id IS NULL) = (exited_at IS NULL)),
    CHECK (exited_at IS NULL OR
           (entered_at IS NOT NULL AND exited_at >= entered_at
            AND assignment_released_at IS NOT NULL AND assignment_released_at <= exited_at)),
    CHECK (canceled_at IS NULL OR
           (exited_at IS NULL AND canceled_at >= assigned_at
            AND assignment_released_at IS NOT NULL AND assignment_released_at <= canceled_at))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS SESSION_FLIGHT (
    session_id VARCHAR(36) NOT NULL,
    flight_id BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY (session_id, flight_id),
    FOREIGN KEY (session_id) REFERENCES PARKING_SESSION (session_id),
    FOREIGN KEY (flight_id) REFERENCES FLIGHT (flight_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sensor-confirmed stays preserve wrong-space parking and relocations.
CREATE TABLE IF NOT EXISTS SPACE_STAY (
    stay_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL,
    actual_space_id VARCHAR(32) NOT NULL,
    parked_at DATETIME(6) NOT NULL,
    vacated_at DATETIME(6) NULL,
    occupied_space VARCHAR(32) GENERATED ALWAYS AS
        (CASE WHEN vacated_at IS NULL THEN actual_space_id END) VIRTUAL,
    parked_session VARCHAR(36) GENERATED ALWAYS AS
        (CASE WHEN vacated_at IS NULL THEN session_id END) VIRTUAL,
    UNIQUE KEY uq_occupied_space (occupied_space),
    UNIQUE KEY uq_parked_session (parked_session),
    UNIQUE KEY uq_stay_event (session_id, parked_at),
    KEY idx_space_occupancy (actual_space_id, parked_at, vacated_at),
    FOREIGN KEY (session_id) REFERENCES PARKING_SESSION (session_id),
    FOREIGN KEY (actual_space_id) REFERENCES PARKING_SPACE (space_id),
    CHECK (vacated_at IS NULL OR vacated_at >= parked_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Settlement attempts distinguish payment failures from exit authorization.
CREATE TABLE IF NOT EXISTS SETTLEMENT (
    settlement_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL,
    settlement_method VARCHAR(32) NOT NULL,
    parking_fee DECIMAL(12, 2) NOT NULL,
    fine_amount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    status ENUM('PENDING', 'SUCCEEDED', 'FAILED') NOT NULL,
    gate_allowed BOOLEAN NOT NULL DEFAULT FALSE,
    recorded_at DATETIME(6) NOT NULL,
    authorized_session VARCHAR(36) GENERATED ALWAYS AS
        (CASE WHEN gate_allowed THEN session_id END) VIRTUAL,
    UNIQUE KEY uq_exit_authorization (authorized_session),
    KEY idx_session_settlements (session_id, recorded_at),
    FOREIGN KEY (session_id) REFERENCES PARKING_SESSION (session_id),
    CHECK (parking_fee >= 0 AND fine_amount >= 0),
    CHECK (NOT gate_allowed OR status = 'SUCCEEDED')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Alerts and patrol reports can exist without an identified vehicle or session.
CREATE TABLE IF NOT EXISTS INCIDENT (
    incident_id VARCHAR(36) NOT NULL PRIMARY KEY,
    session_id VARCHAR(36) NULL,
    space_id VARCHAR(32) NULL,
    zone_id VARCHAR(20) NULL,
    reported_plate VARCHAR(32) NULL,
    incident_type ENUM('CAPACITY_WARNING', 'MISPARKING', 'ACCESSIBLE_SPACE_VIOLATION',
                       'DRIVING_TIMEOUT', 'PARKING_TIMEOUT', 'DEVICE_FAILURE', 'PATROL_REPORT') NOT NULL,
    message VARCHAR(1000) NOT NULL,
    detected_at DATETIME(6) NOT NULL,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    KEY idx_recent_incidents (detected_at, incident_id),
    FOREIGN KEY (session_id) REFERENCES PARKING_SESSION (session_id),
    FOREIGN KEY (space_id) REFERENCES PARKING_SPACE (space_id),
    FOREIGN KEY (zone_id) REFERENCES PARKING_ZONE (zone_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Demonstration layout and reference data. Reimports preserve existing settings.
START TRANSACTION;

INSERT INTO PARKING_ZONE (zone_id, zone_name, description, capacity)
VALUES
    ('A', 'Zone A', 'Near Terminal A', 4),
    ('B', 'Zone B', 'Near Terminal B', 4),
    ('C', 'Zone C', 'Long-stay and remote parking', 4)
ON DUPLICATE KEY UPDATE
    zone_id = PARKING_ZONE.zone_id;

INSERT INTO TERMINAL (terminal_id, terminal_name) VALUES
    ('A', 'Terminal A'), ('B', 'Terminal B')
ON DUPLICATE KEY UPDATE terminal_id = TERMINAL.terminal_id;

INSERT INTO GATE (gate_id, gate_type) VALUES
    ('EAST', 'BOTH'), ('WEST', 'BOTH'), ('SOUTH', 'BOTH')
ON DUPLICATE KEY UPDATE gate_id = GATE.gate_id;

INSERT INTO PARKING_SPACE
    (space_id, zone_id, aisle_code, space_number, label, space_type, long_stay, `accessible`, ev_charging)
VALUES
    ('A01', 'A', 'X', '01', 'A01', 'GENERAL', FALSE, FALSE, TRUE),
    ('A02', 'A', 'X', '02', 'A02', 'GENERAL', FALSE, TRUE, FALSE),
    ('A03', 'A', 'X', '03', 'A03', 'MOTORCYCLE', FALSE, FALSE, FALSE),
    ('A04', 'A', 'X', '04', 'A04', 'EMPLOYEE_RESERVED', FALSE, TRUE, FALSE),
    ('B01', 'B', 'X', '01', 'B01', 'GENERAL', FALSE, FALSE, TRUE),
    ('B02', 'B', 'X', '02', 'B02', 'GENERAL', FALSE, TRUE, FALSE),
    ('B03', 'B', 'X', '03', 'B03', 'AIRLINE_RESERVED', FALSE, FALSE, FALSE),
    ('B04', 'B', 'X', '04', 'B04', 'COMMERCIAL', FALSE, FALSE, FALSE),
    ('C01', 'C', 'X', '01', 'C01', 'GENERAL', TRUE, FALSE, TRUE),
    ('C02', 'C', 'X', '02', 'C02', 'GENERAL', TRUE, TRUE, FALSE),
    ('C03', 'C', 'X', '03', 'C03', 'MOTORCYCLE', TRUE, FALSE, FALSE),
    ('C04', 'C', 'X', '04', 'C04', 'PROFESSIONAL_TRANSPORT', FALSE, FALSE, FALSE)
ON DUPLICATE KEY UPDATE space_id = PARKING_SPACE.space_id;

INSERT INTO SPACE_TERMINAL (space_id, terminal_id, walking_distance_m) VALUES
    ('A01', 'A', 100), ('A02', 'A', 80), ('A03', 'A', 140), ('A04', 'A', 90),
    ('B01', 'B', 100), ('B02', 'B', 80), ('B03', 'B', 140), ('B04', 'B', 160),
    ('C01', 'A', 550), ('C02', 'A', 500), ('C03', 'A', 580), ('C04', 'A', 450),
    ('C01', 'B', 560), ('C02', 'B', 510), ('C03', 'B', 590), ('C04', 'B', 440)
ON DUPLICATE KEY UPDATE walking_distance_m = SPACE_TERMINAL.walking_distance_m;

INSERT INTO SPACE_GATE (space_id, gate_id, driving_distance_m)
SELECT s.space_id, g.gate_id,
       CASE
           WHEN (s.zone_id = 'A' AND g.gate_id = 'EAST')
             OR (s.zone_id = 'B' AND g.gate_id = 'WEST')
             OR (s.zone_id = 'C' AND g.gate_id = 'SOUTH') THEN 200
           ELSE 650
       END + 20 * CAST(RIGHT(s.space_id, 2) AS UNSIGNED)
FROM PARKING_SPACE s CROSS JOIN GATE g
WHERE s.space_id IN ('A01', 'A02', 'A03', 'A04', 'B01', 'B02', 'B03', 'B04',
                     'C01', 'C02', 'C03', 'C04')
  AND g.gate_id IN ('EAST', 'WEST', 'SOUTH')
ON DUPLICATE KEY UPDATE driving_distance_m = SPACE_GATE.driving_distance_m;

INSERT INTO AIRLINE (airline_id, airline_name) VALUES
    ('MA', 'MIA Air'), ('ST', 'Sky Transport')
ON DUPLICATE KEY UPDATE airline_id = AIRLINE.airline_id;

INSERT INTO FLIGHT (airline_id, terminal_id, flight_number, direction, scheduled_at) VALUES
    ('MA', 'A', 'MA101', 'DEPARTURE', '2026-11-26 10:00:00'),
    ('MA', 'B', 'MA102', 'ARRIVAL', '2026-11-26 12:30:00'),
    ('ST', 'B', 'ST201', 'DEPARTURE', '2026-11-26 16:00:00'),
    ('ST', 'A', 'ST202', 'ARRIVAL', '2026-11-26 18:30:00')
ON DUPLICATE KEY UPDATE flight_id = FLIGHT.flight_id;

INSERT INTO MEMBERSHIP (membership_id, customer_category, valid_from) VALUES
    ('DEMO-SUB', 'SUBSCRIBER', '2026-01-01 00:00:00'),
    ('DEMO-EMP', 'AIRPORT_EMPLOYEE', '2026-01-01 00:00:00'),
    ('DEMO-AIR', 'AIRLINE_STAFF', '2026-01-01 00:00:00'),
    ('DEMO-TRN', 'PROFESSIONAL_TRANSPORT', '2026-01-01 00:00:00')
ON DUPLICATE KEY UPDATE membership_id = MEMBERSHIP.membership_id;

COMMIT;
