-- Initial Tester Accounts
INSERT INTO "user" (username, enabled, password) VALUES
('USER1', true, '1234'),
('USER2', true, '1234'),
('USER3', true, '1234'),
('USER4', true, '1234'),
('USER5', true, '1234'),
('USER6', true, '1234'),
('USER7', true, '1234');


INSERT INTO authority (username, authority) VALUES
('USER1', 'USER'),('USER1', 'ADMIN'),('USER1', 'ATC'),
('USER2', 'USER'),('USER2', 'ADMIN'),
('USER3', 'USER'),('USER3', 'ATC'),
('USER4', 'ADMIN'),('USER4', 'ATC'),
('USER5', 'USER'),('USER6', 'ADMIN'),('USER7', 'ATC');


/*
    USER1, has all authorities (USER, ADMIN, ATC)
    USER2, has (USER, ADMIN)
    USER3, has (USER, ATC)
    USER4, has (ADMIN, ATC)
    USER5, has (USER)
    USER6, has (ADMIN)
    USER7, has (ATC)

    This way there is an account for every scenario
*/

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
('FFA12345', 'Boeing 737', 215, 'Schiphol'),('FFA12346', 'Boeing 747', 416, 'Heathrow'),('FFA12347', 'Boeing 757', 295, 'Hartsfield-Jackson'),
('FFA12348', 'Boeing 767', 375, 'Beijing Capital'),('FFA12349', 'Boeing 777', 550, 'Dubai International'),('FFA12350', 'Boeing 787', 330, 'Tokyo Haneda'),
('FFA12351', 'Airbus A300', 266, 'Los Angeles International'),('FFA12352', 'Airbus A310', 280, 'O Hare International'),('FFA12353', 'Airbus A320', 180, 'Frankfurt'),
('FFA12354', 'Airbus A330', 335, 'Singapore Changi'),('FFA12355', 'Airbus A340', 440, 'Incheon International'),('FFA12356', 'Airbus A350', 325, 'Denver International'),
('FFA12357', 'Airbus A380', 853, 'Hong Kong International'),('FFA12358', 'Embraer E170', 80, 'Sydney Kingsford Smith'),('FFA12359', 'Embraer E175', 88, 'Doha Hamad'),
('FFA12360', 'Embraer E190', 106, 'Schiphol'),('FFA12361', 'Embraer E195', 124, 'Heathrow'),('FFA12362', 'Bombardier CRJ200', 50, 'Hartsfield-Jackson'),
('FFA12363', 'Bombardier CRJ700', 70, 'Beijing Capital'),('FFA12364', 'Bombardier CRJ900', 90, 'Dubai International'),('FFA12365', 'Bombardier CRJ1000', 104, 'Tokyo Haneda'),
('FFA12366', 'ATR 42', 48, 'Los Angeles International'),('FFA12367', 'ATR 72', 78, 'O Hare International'),('FFA12368', 'BAe 146', 112, 'Frankfurt'),
('FFA12369', 'BAe Avro RJ70', 70, 'Singapore Changi'),('FFA12370', 'BAe Avro RJ85', 85, 'Incheon International'),('FFA12371', 'BAe Avro RJ100', 100, 'Denver International'),
('FFA12372', 'BAe Jetstream 31', 19, 'Hong Kong International'),('FFA12373', 'BAe Jetstream 41', 29, 'Sydney Kingsford Smith'),('FFA12374', 'Fokker 50', 58, 'Doha Hamad'),
('FFA12375', 'Fokker 70', 80, 'Schiphol'),('FFA12376', 'Fokker 100', 109, 'Heathrow'),('FFA12377', 'Saab 340', 34, 'Hartsfield-Jackson'),
('FFA12378', 'Saab 2000', 50, 'Beijing Capital'),('FFA12379', 'Cessna 208', 9, 'Dubai International'),('FFA12380', 'Cessna 402', 9, 'Tokyo Haneda'),
('FFA12381', 'Cessna 404', 9, 'Los Angeles International'),('FFA12382', 'Cessna 414', 9, 'O Hare International'),('FFA12383', 'Cessna 421', 9, 'Frankfurt'),
('FFA12384', 'Cessna 441', 9, 'Singapore Changi'),('FFA12385', 'Cessna Citation', 9, 'Incheon International'),('FFA12386', 'Cessna Conquest', 9, 'Denver International'),
('FFA12387', 'Cessna Mustang', 9, 'Hong Kong International'),('FFA12388', 'Cessna Skyhawk', 9, 'Sydney Kingsford Smith'),('FFA12389', 'Cessna Skylane', 9, 'Doha Hamad'),
('FFA12390', 'Cessna Stationair', 9, 'Schiphol'),('FFA12391', 'Cessna TTx', 9, 'Heathrow'),('FFA12392', 'Cessna Turbo Stationair', 9, 'Hartsfield-Jackson'),
('FFA12393', 'Cessna Turbo Skylane', 9, 'Beijing Capital'),('FFA12394', 'Cessna Turbo Stationair', 9, 'Dubai International'),
('FFA12395', 'Cessna Testing', 3, 'Dubai International');


-- Initial flights
INSERT INTO flights (flight_number, estimated_time_of_departure, estimated_time_of_arrival, airplane_code, departure_airport_name, arrival_airport_name, filled_seats) VALUES
('TESTFLIGHT1', '2025-01-01 12:00:00', '2025-01-01 14:00:00', 'FFA12395', 'Dubai International', 'Tokyo Haneda', 0),
('TESTFLIGHT2', '2025-02-01 12:00:00', '2025-02-01 14:00:00', 'FFA12395', 'Tokyo Haneda', 'Doha Hamad', 0),
('TESTFLIGHT3', '2025-03-01 12:00:00', '2025-03-01 14:00:00', 'FFA12395', 'Doha Hamad', 'Dubai International', 0);