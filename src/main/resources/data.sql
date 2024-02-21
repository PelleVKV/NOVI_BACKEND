-- Initial Tester Accounts
INSERT INTO "user" (username, enabled, password) VALUES
('Pelle1', true, '1234'),
('Pelle2', true, '1234'),
('Pelle3', true, '1234');

INSERT INTO authority (username, authority) VALUES
('Pelle1', 'USER'),
('Pelle1', 'ADMIN'),
('Pelle2', 'ADMIN'),
('Pelle2', 'USER');

-- Initial Database Data
INSERT INTO airports (airport_name, airport_country, airport_city) VALUES
('Schiphol', 'Netherlands', 'Amsterdam'),
('Heathrow', 'United Kingdom', 'London'),
('Hartsfield-Jackson', 'United States', 'Atlanta'),
('Beijing Capital', 'China', 'Beijing'),
('Dubai International', 'United Arab Emirates', 'Dubai'),
('Tokyo Haneda', 'Japan', 'Tokyo'),
('Los Angeles International', 'United States', 'Los Angeles'),
('O Hare International', 'United States', 'Chicago'),
('Frankfurt', 'Germany', 'Frankfurt'),
('Singapore Changi', 'Singapore', 'Singapore'),
('Incheon International', 'South Korea', 'Seoul'),
('Denver International', 'United States', 'Denver'),
('Hong Kong International', 'Hong Kong', 'Hong Kong'),
('Sydney Kingsford Smith', 'Australia', 'Sydney'),
('Doha Hamad', 'Qatar', 'Doha');

-- Initial airplanes
INSERT INTO airplanes (airplane_code, airplane_type, airplane_capacity, airport_name) VALUES
('FFA0001', 'Boeing 737', 215, 'Schiphol'),('FFA0002', 'Boeing 747', 416, 'Heathrow'),('FFA0003', 'Boeing 757', 295, 'Hartsfield-Jackson'),
('FFA0004', 'Boeing 767', 375, 'Beijing Capital'),('FFA0005', 'Boeing 777', 550, 'Dubai International'),('FFA0006', 'Boeing 787', 330, 'Tokyo Haneda'),
('FFA0007', 'Airbus A300', 266, 'Los Angeles International'),('FFA0008', 'Airbus A310', 280, 'O Hare International'),('FFA0009', 'Airbus A320', 180, 'Frankfurt'),
('FFA0010', 'Airbus A330', 335, 'Singapore Changi'),('FFA0011', 'Airbus A340', 440, 'Incheon International'),('FFA0012', 'Airbus A350', 325, 'Denver International'),
('FFA0013', 'Airbus A380', 853, 'Hong Kong International'),('FFA0014', 'Embraer E170', 80, 'Sydney Kingsford Smith'),('FFA0015', 'Embraer E175', 88, 'Doha Hamad'),
('FFA0016', 'Embraer E190', 106, 'Schiphol'),('FFA0017', 'Embraer E195', 124, 'Heathrow'),('FFA0018', 'Bombardier CRJ200', 50, 'Hartsfield-Jackson'),
('FFA0019', 'Bombardier CRJ700', 70, 'Beijing Capital'),('FFA0020', 'Bombardier CRJ900', 90, 'Dubai International'),('FFA0021', 'Bombardier CRJ1000', 104, 'Tokyo Haneda'),
('FFA0022', 'ATR 42', 48, 'Los Angeles International'),('FFA0023', 'ATR 72', 78, 'O Hare International'),('FFA0024', 'BAe 146', 112, 'Frankfurt'),
('FFA0025', 'BAe Avro RJ70', 70, 'Singapore Changi'),('FFA0026', 'BAe Avro RJ85', 85, 'Incheon International'),('FFA0027', 'BAe Avro RJ100', 100, 'Denver International'),
('FFA0028', 'BAe Jetstream 31', 19, 'Hong Kong International'),('FFA0029', 'BAe Jetstream 41', 29, 'Sydney Kingsford Smith'),('FFA0030', 'Fokker 50', 58, 'Doha Hamad'),
('FFA0031', 'Fokker 70', 80, 'Schiphol'),('FFA0032', 'Fokker 100', 109, 'Heathrow'),('FFA0033', 'Saab 340', 34, 'Hartsfield-Jackson'),
('FFA0034', 'Saab 2000', 50, 'Beijing Capital'),('FFA0035', 'Cessna 208', 9, 'Dubai International'),('FFA0036', 'Cessna 402', 9, 'Tokyo Haneda'),
('FFA0037', 'Cessna 404', 9, 'Los Angeles International'),('FFA0038', 'Cessna 414', 9, 'O Hare International'),('FFA0039', 'Cessna 421', 9, 'Frankfurt'),
('FFA0040', 'Cessna 441', 9, 'Singapore Changi'),('FFA0041', 'Cessna Citation', 9, 'Incheon International'),('FFA0042', 'Cessna Conquest', 9, 'Denver International'),
('FFA0043', 'Cessna Mustang', 9, 'Hong Kong International'),('FFA0044', 'Cessna Skyhawk', 9, 'Sydney Kingsford Smith'),('FFA0045', 'Cessna Skylane', 9, 'Doha Hamad'),
('FFA0046', 'Cessna Stationair', 9, 'Schiphol'),('FFA0047', 'Cessna TTx', 9, 'Heathrow'),('FFA0048', 'Cessna Turbo Stationair', 9, 'Hartsfield-Jackson'),
('FFA0049', 'Cessna Turbo Skylane', 9, 'Beijing Capital'),('FFA0050', 'Cessna Turbo Stationair', 9, 'Dubai International');

-- Initial flights
INSERT INTO flights (flight_number, estimated_time_of_departure, estimated_time_of_arrival, airplane_code, departure_airport_name, arrival_airport_name) VALUES
('FFA12010001SCHE', '2024-12-01 08:00:00', '2023-12-01 10:00:00', 'FFA0001', 'Schiphol', 'Heathrow'),
('FFA12040003HABE', '2024-12-04 10:30:00', '2023-12-02 14:00:00', 'FFA0003', 'Hartsfield-Jackson', 'Beijing Capital'),
('FFA12090005DUTO', '2024-12-09 12:15:00', '2023-12-03 18:30:00', 'FFA0005', 'Dubai International', 'Tokyo Haneda'),
('FFA12140007LOOH', '2024-12-14 15:45:00', '2023-12-04 20:30:00', 'FFA0007', 'Los Angeles International', 'O Hare International'),
('FFA01040009FRSI', '2025-01-04 18:30:00', '2024-12-05 22:45:00', 'FFA0009', 'Frankfurt', 'Singapore Changi'),
('FFA01060011ICDE', '2025-01-06 20:00:00', '2024-12-06 23:30:00', 'FFA0011', 'Incheon International', 'Denver International'),
('FFA01070013HOSY', '2025-01-07 22:45:00', '2024-12-08 03:15:00', 'FFA0013', 'Hong Kong International', 'Sydney Kingsford Smith'),
('FFA01120015DOSC', '2025-01-12 01:30:00', '2024-12-09 04:45:00', 'FFA0015', 'Doha Hamad', 'Schiphol'),
('FFA02100017HEHA', '2025-02-10 04:15:00', '2024-12-10 07:30:00', 'FFA0017', 'Heathrow', 'Hartsfield-Jackson'),
('FFA02110019BEDU', '2025-02-11 07:45:00', '2024-12-11 11:00:00', 'FFA0019', 'Beijing Capital', 'Dubai International');
