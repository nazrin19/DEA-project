-- ============================================================
--  data.sql – LankaTools DEA Project (Person 6 Deliverable)
--  Seed Data: 1 Admin, 3 Shop Owners, 5 Customers
--  All user passwords are encrypted: "password123"
-- ============================================================

-- 1. SEED USERS
INSERT INTO users (id, name, email, password, role, shop_name, shop_address, phone, is_approved, is_active, created_at) VALUES
(1, 'Admin User', 'admin@dea.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'ADMIN', NULL, NULL, '0771234567', TRUE, TRUE, '2025-01-01 08:00:00'),

-- Shop Owners
(2, 'Ahmed Silva', 'ahmed@shop.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'SHOP_OWNER', 'Ahmed Tools', '12 Galle Road, Colombo 03', '0777654321', TRUE, TRUE, '2025-01-02 09:00:00'),
(3, 'Sara Perera', 'sara@shop.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'SHOP_OWNER', 'Sara Rentals', '45 Kandy Road, Kandy', '0712223344', TRUE, TRUE, '2025-01-03 09:30:00'),
(4, 'Kamal Fernando', 'kamal@shop.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'SHOP_OWNER', 'Kamal Store', '78 Marine Drive, Negombo', '0769988776', TRUE, TRUE, '2025-01-04 10:00:00'),

-- Customers
(5, 'Nimal Jayasena', 'nimal@gmail.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'CUSTOMER', NULL, NULL, '0781112233', TRUE, TRUE, '2025-01-05 11:00:00'),
(6, 'Dilsha Madushani', 'dilsha@gmail.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'CUSTOMER', NULL, NULL, '0754433221', TRUE, TRUE, '2025-01-06 11:30:00'),
(7, 'Ruwan Bandara', 'ruwan@gmail.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'CUSTOMER', NULL, NULL, '0765566778', TRUE, TRUE, '2025-01-07 12:00:00'),
(8, 'Priya Kumari', 'priya@gmail.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'CUSTOMER', NULL, NULL, '0776677889', TRUE, TRUE, '2025-01-08 12:30:00'),
(9, 'Lasith Malinga', 'lasith@gmail.com', '$2a$10$R7M5u9r1gEwVmqB.PZsnO.o8z1XbS9R82y1K4M1V2w3x4y5z6a7b.', 'CUSTOMER', NULL, NULL, '0787788990', TRUE, TRUE, '2025-01-09 13:00:00');


-- 2. SEED TOOLS (Matches Person 2 Entity Variable: daily_rate mapped from dailyRate)
INSERT INTO tools (id, name, description, category, daily_rate, image_url, status, user_id) VALUES
-- Ahmed's Tools (user_id = 2)
(1,  'Bosch Rotary Hammer', 'Heavy-duty rotary hammer drill, 800W, SDS-Plus chuck. Ideal for concrete and masonry work.', 'Drilling', 1500.00, '/images/tools/rotary-hammer.jpg', 'APPROVED', 2),
(2,  'Angle Grinder 9 Inch', '2200W angle grinder with disc guard. Suitable for cutting tiles, metal, and concrete.', 'Cutting', 900.00, '/images/tools/angle-grinder.jpg', 'APPROVED', 2),
(3,  'Pressure Washer 2000PSI', 'Electric pressure washer with 8m hose. Great for driveways, vehicles, and walls.', 'Cleaning', 2000.00, '/images/tools/pressure-washer.jpg', 'APPROVED', 2),
(4,  'Electric Tile Cutter', 'Wet tile cutter with sliding table, 600mm cut capacity. Perfect for ceramic and porcelain tiles.', 'Cutting', 1800.00, '/images/tools/tile-cutter.jpg', 'PENDING', 2),
(5,  'Concrete Mixer 140L', 'Portable drum mixer, 1-phase 230V motor, 140L drum. Ideal for small to mid construction jobs.', 'Mixing', 2500.00, '/images/tools/concrete-mixer.jpg', 'APPROVED', 2),

-- Sara's Tools (user_id = 3)
(6,  'Scaffold Tower 4m', 'Aluminium mobile scaffold tower. Max working height 4m, weight capacity 200kg.', 'Access', 3500.00, '/images/tools/scaffold-tower.jpg', 'APPROVED', 3),
(7,  'Petrol Chainsaw 18 Inch', 'Petrol chainsaw, 45cc engine, 18 inch guide bar. Suitable for tree felling and logging.', 'Cutting', 2200.00, '/images/tools/chainsaw.jpg', 'APPROVED', 3),
(8,  'Plate Compactor', 'Honda-powered plate compactor for soil and asphalt compaction. 60kg base plate.', 'Compaction', 3000.00, '/images/tools/plate-compactor.jpg', 'APPROVED', 3),
(9,  'Laser Level Kit', 'Self-levelling cross-line laser with tripod. Range 30m, accuracy plus or minus 0.3mm per metre.', 'Measuring', 1200.00, '/images/tools/laser-level.jpg', 'APPROVED', 3),
(10, 'Electric Jackhammer', '1500W demolition jackhammer with chisel bits. Ideal for breaking up concrete slabs.', 'Demolition', 2800.00, '/images/tools/jackhammer.jpg', 'REJECTED', 3),

-- Kamal's Tools (user_id = 4)
(11, 'Air Compressor 50L', '2HP direct-drive air compressor, 50L tank, max 8 bar. Powers air tools and spray guns.', 'Pneumatic', 1600.00, '/images/tools/air-compressor.jpg', 'APPROVED', 4),
(12, 'Cordless Drill Set', 'Makita 18V brushless drill and impact driver combo kit with 2 batteries and charger.', 'Drilling', 700.00, '/images/tools/cordless-drill.jpg', 'APPROVED', 4),
(13, 'Generator 3.5kVA', 'Petrol generator, 3.5kVA rated output, AVR stabiliser. Silent type enclosure.', 'Power', 4000.00, '/images/tools/generator.jpg', 'APPROVED', 4),
(14, 'Pipe Threading Machine', 'Electric pipe threader, capacity 1/2 inch to 2 inch, with die heads and cutting oil.', 'Plumbing', 2200.00, '/images/tools/pipe-threader.jpg', 'APPROVED', 4),
(15, 'Floor Sander', 'Drum floor sander for hardwood floor refinishing. 230V, 1500W, dust bag included.', 'Finishing', 1900.00, '/images/tools/floor-sander.jpg', 'PENDING', 4);


-- 3. SEED BOOKINGS (Matches Person 3 Entity explicit @Column mappings)
INSERT INTO bookings (id, start_date, end_date, total_cost, status, customer_id, tool_id) VALUES
-- Nimal (customer_id = 5)
(1,  '2025-06-01', '2025-06-03', 4500.00,  'RETURNED',  5, 1),
(2,  '2025-06-10', '2025-06-12', 10500.00, 'CONFIRMED', 5, 6),

-- Dilsha (customer_id = 6)
(3,  '2025-06-05', '2025-06-06', 2000.00,  'RETURNED',  6, 3),
(4,  '2025-06-20', '2025-06-22', 12000.00, 'ACTIVE',    6, 13),

-- Ruwan (customer_id = 7)
(5,  '2025-06-08', '2025-06-09', 900.00,   'CANCELLED', 7, 2),
(6,  '2025-06-15', '2025-06-17', 3600.00,  'CONFIRMED', 7, 9),

-- Priya (customer_id = 8)
(7,  '2025-06-02', '2025-06-04', 7500.00,  'RETURNED',  8, 5),
(8,  '2025-06-18', '2025-06-20', 4800.00,  'PENDING',   8, 11),

-- Lasith (customer_id = 9)
(9,  '2025-06-12', '2025-06-13', 700.00,   'REJECTED',  9, 12),
(10, '2025-06-25', '2025-06-27', 9000.00,  'PENDING',   9, 8);