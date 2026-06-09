-- V1__create_initial_schema.sql

CREATE TABLE rooms (
    id              BIGSERIAL PRIMARY KEY,
    room_number     VARCHAR(10)    NOT NULL UNIQUE,
    room_type       VARCHAR(20)    NOT NULL,
    floor           INT            NOT NULL CHECK (floor >= 1),
    capacity        INT            NOT NULL CHECK (capacity >= 1),
    price_per_night NUMERIC(10,2)  NOT NULL CHECK (price_per_night > 0),
    available       BOOLEAN        NOT NULL DEFAULT TRUE
);

CREATE TABLE guests (
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(100)  NOT NULL,
    last_name   VARCHAR(100)  NOT NULL,
    email       VARCHAR(200)  NOT NULL UNIQUE,
    phone       VARCHAR(20)
);

CREATE TABLE reservations (
    id             BIGSERIAL PRIMARY KEY,
    guest_id       BIGINT         NOT NULL REFERENCES guests(id),
    room_id        BIGINT         NOT NULL REFERENCES rooms(id),
    check_in_date  DATE           NOT NULL,
    check_out_date DATE           NOT NULL,
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    total_price    NUMERIC(10,2)  NOT NULL CHECK (total_price > 0),
    created_at     TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP      NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_dates CHECK (check_out_date > check_in_date)
);

CREATE TABLE payments (
    id             BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT         NOT NULL UNIQUE REFERENCES reservations(id),
    amount         NUMERIC(10,2)  NOT NULL CHECK (amount > 0),
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    paid_at        TIMESTAMP,
    created_at     TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_reservations_guest_id   ON reservations(guest_id);
CREATE INDEX idx_reservations_room_id    ON reservations(room_id);
CREATE INDEX idx_reservations_status     ON reservations(status);
CREATE INDEX idx_reservations_check_in   ON reservations(check_in_date);
CREATE INDEX idx_reservations_check_out  ON reservations(check_out_date);
