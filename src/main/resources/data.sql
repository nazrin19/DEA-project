-- ============================================================
--  data.sql – LankaTools Production Seed Data (Perfect Alignment)
--  All user passwords resolve to: "password123"
--  Booking cost uses Inclusive Math: (end - start) + 1
-- ============================================================

-- 1. SEED USERS (Verified 60-character BCrypt hashes for "password123")
INSERT IGNORE INTO users (id, name, email, password, role, shop_name, shop_address, phone, is_approved, is_active, created_at) VALUES
(1, 'Admin User',       'admin@dea.com',   '$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'ADMIN',     NULL,          NULL,                          '0771234567', TRUE, TRUE, '2025-01-01 08:00:00'),
(2, 'Ahmed Silva',      'ahmed@shop.com',  '$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'SHOP_OWNER', 'Ahmed Tools', '12 Galle Road, Colombo 03',   '0777654321', TRUE, TRUE, '2025-01-02 09:00:00'),
(3, 'Sara Perera',      'sara@shop.com',   '$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'SHOP_OWNER', 'Sara Rentals','45 Kandy Road, Kandy',        '0712223344', TRUE, TRUE, '2025-01-03 09:30:00'),
(4, 'Kamal Fernando',   'kamal@shop.com',  '$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'SHOP_OWNER', 'Kamal Store', '78 Marine Drive, Negombo',    '0769988776', TRUE, TRUE, '2025-01-04 10:00:00'),
(5, 'Nimal Jayasena',   'nimal@gmail.com', '$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'CUSTOMER',   NULL,          NULL,                          '0781112233', TRUE, TRUE, '2025-01-05 11:00:00'),
(6, 'Dilsha Madushani', 'dilsha@gmail.com','$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'CUSTOMER',   NULL,          NULL,                          '0754433221', TRUE, TRUE, '2025-01-06 11:30:00'),
(7, 'Ruwan Bandara',     'ruwan@gmail.com', '$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'CUSTOMER',   NULL,          NULL,                          '0765566778', TRUE, TRUE, '2025-01-07 12:00:00'),
(8, 'Priya Kumari',      'priya@gmail.com', '$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'CUSTOMER',   NULL,          NULL,                          '0776677889', TRUE, TRUE, '2025-01-08 12:30:00'),
(9, 'Lasith Malinga',    'lasith@gmail.com','$2a$12$tO5zbk1VmTzF1XDji5SZuuVw9c8E0tfNNmg6wXWl65DjCv4AJc94y', 'CUSTOMER',   NULL,          NULL,                          '0787788990', TRUE, TRUE, '2025-01-09 13:00:00');


-- 2. SEED TOOLS
INSERT IGNORE INTO tools (id, name, description, category, daily_rate, image_url, status, user_id) VALUES
-- Ahmed's Tools (user_id = 2)
(1,  'Bosch Rotary Hammer',     'Heavy-duty rotary hammer drill, 800W, SDS-Plus chuck.',              'Drilling',    1500.00, '/images/tools/rotary-hammer.jpg',   'APPROVED', 2),
(2,  'Angle Grinder 9 Inch',    '2200W angle grinder with disc guard.',                               'Cutting',      900.00, '/images/tools/angle-grinder.jpg',   'APPROVED', 2),
(3,  'Pressure Washer 2000PSI', 'Electric pressure washer with 8m hose.',                             'Cleaning',    2000.00, '/images/tools/pressure-washer.jpg', 'APPROVED', 2),
(4,  'Electric Tile Cutter',    'Wet tile cutter with sliding table, 600mm cut capacity.',            'Cutting',     1800.00, '/images/tools/tile-cutter.jpg',     'PENDING',  2),
(5,  'Concrete Mixer 140L',     'Portable drum mixer, 1-phase 230V motor, 140L drum.',                'Mixing',      2500.00, '/images/tools/concrete-mixer.jpg',  'APPROVED', 2),

-- Sara's Tools (user_id = 3)
(6,  'Scaffold Tower 4m',       'Aluminium mobile scaffold tower. Max working height 4m.',            'Access',      3500.00, '/images/tools/scaffold-tower.jpg',  'APPROVED', 3),
(7,  'Petrol Chainsaw 18 Inch', 'Petrol chainsaw, 45cc engine, 18 inch guide bar.',                  'Cutting',     2200.00, '/images/tools/chainsaw.jpg',        'APPROVED', 3),
(8,  'Plate Compactor',         'Honda-powered plate compactor for soil compaction.',                  'Compaction',  3000.00, '/images/tools/plate-compactor.jpg', 'APPROVED', 3),
(9,  'Laser Level Kit',         'Self-levelling cross-line laser with tripod.',                       'Measuring',   1200.00, '/images/tools/laser-level.jpg',     'APPROVED', 3),
(10, 'Electric Jackhammer',     '1500W demolition jackhammer with chisel bits.',                     'Demolition',  2800.00, '/images/tools/jackhammer.jpg',      'REJECTED', 3),

-- Kamal's Tools (user_id = 4)
(11, 'Air Compressor 50L',      '2HP direct-drive air compressor, 50L tank, max 8 bar.',             'Pneumatic',   1600.00, '/images/tools/air-compressor.jpg',  'APPROVED', 4),
(12, 'Cordless Drill Set',      'Makita 18V brushless drill and impact driver combo kit.',            'Drilling',     700.00, '/images/tools/cordless-drill.jpg',  'APPROVED', 4),
(13, 'Generator 3.5kVA',        'Petrol generator, 3.5kVA rated output, AVR stabiliser.',            'Power',       4000.00, '/images/tools/generator.jpg',       'APPROVED', 4),
(14, 'Pipe Threading Machine',  'Electric pipe threader, capacity 1/2 inch to 2 inch.',              'Plumbing',    2200.00, '/images/tools/pipe-threader.jpg',   'APPROVED', 4),
(15, 'Floor Sander',            'Drum floor sander for hardwood floor refinishing.',                  'Finishing',   1900.00, '/images/tools/floor-sander.jpg',    'PENDING',  4);


-- 3. SEED BOOKINGS (Fixed Booking 1 Value)
INSERT IGNORE INTO bookings (id, start_date, end_date, total_cost, status, customer_id, tool_id) VALUES
-- Nimal (customer_id = 5)
(1,  '2025-06-01', '2025-06-03',  4500.00, 'RETURNED',  5,  1),   -- FIXED: 3 days × 1500 = 4500
(2,  '2025-06-10', '2025-06-12', 10500.00, 'CONFIRMED', 5,  6),   -- 3 days × 3500 = 10500

-- Dilsha (customer_id = 6)
(3,  '2025-06-05', '2025-06-06',  4000.00, 'RETURNED',  6,  3),   -- 2 days × 2000 = 4000
(4,  '2025-06-20', '2025-06-22', 12000.00, 'ACTIVE',    6,  13),  -- 3 days × 4000 = 12000

-- Ruwan (customer_id = 7)
(5,  '2025-06-08', '2025-06-09',  1800.00, 'CANCELLED', 7,  2),   -- 2 days × 900  = 1800
(6,  '2025-06-15', '2025-06-17',  3600.00, 'CONFIRMED', 7,  9),   -- 3 days × 1200 = 3600

-- Priya (customer_id = 8)
(7,  '2025-06-02', '2025-06-04',  7500.00, 'RETURNED',  8,  5),   -- 3 days × 2500 = 7500
(8,  '2025-06-18', '2025-06-20',  4800.00, 'PENDING',   8,  11),  -- 3 days × 1600 = 4800

-- Lasith (customer_id = 9)
(9,  '2025-06-12', '2025-06-13',  1400.00, 'REJECTED',  9,  12),  -- 2 days × 700  = 1400
(10, '2025-06-25', '2025-06-27',  9000.00, 'PENDING',   9,  8);   -- 3 days × 3000 = 9000