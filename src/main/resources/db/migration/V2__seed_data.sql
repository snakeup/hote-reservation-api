-- V2__seed_data.sql

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night) VALUES
    ('101', 'SINGLE',    1, 1,  89.00),
    ('102', 'SINGLE',    1, 1,  89.00),
    ('201', 'DOUBLE',    2, 2, 129.00),
    ('202', 'DOUBLE',    2, 2, 129.00),
    ('301', 'SUITE',     3, 3, 249.00),
    ('302', 'SUITE',     3, 4, 269.00),
    ('401', 'PENTHOUSE', 4, 4, 499.00);

INSERT INTO guests (first_name, last_name, email, phone) VALUES
    ('Alice',  'Johnson', 'alice.johnson@example.com', '+1-555-0101'),
    ('Bob',    'Smith',   'bob.smith@example.com',     '+1-555-0102'),
    ('Carlos', 'García',  'carlos.garcia@example.com', '+52-555-0103');

-- One existing confirmed reservation so overlap detection can be tested
INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date, status, total_price, created_at, updated_at) VALUES
    (1, 1, '2025-09-01', '2025-09-05', 'CONFIRMED', 356.00, NOW(), NOW());

INSERT INTO payments (reservation_id, amount, status, paid_at, created_at) VALUES
    (1, 356.00, 'COMPLETED', NOW(), NOW());
