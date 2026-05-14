DROP DATABASE IF EXISTS aquafit_wellness_center;
CREATE DATABASE aquafit_wellness_center
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
USE aquafit_wellness_center;
CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    dob DATE,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255)
);

CREATE TABLE membership_plans (
    membership_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    duration_months INT NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE trainers (
    trainer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialty VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20)
);

CREATE TABLE physiotherapists (
    physio_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialty VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20)
);

CREATE TABLE health_records (
    health_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    condition_name VARCHAR(150) NOT NULL,
    notes TEXT,
    record_date DATE NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE pools (
    pool_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(150),
    capacity INT NOT NULL
);

CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    room_type VARCHAR(50),
    capacity INT NOT NULL
);

CREATE TABLE schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE member_memberships (
    member_membership_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    membership_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (membership_id) REFERENCES membership_plans(membership_id)
);

CREATE TABLE fitness_classes (
    class_id INT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL,
    schedule_id INT NOT NULL,
    room_id INT NOT NULL,
    trainer_id INT NOT NULL,
    max_capacity INT,
    FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id),
    FOREIGN KEY (trainer_id) REFERENCES trainers(trainer_id)
);

CREATE TABLE class_enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    class_id INT NOT NULL,
    enrollment_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Enrolled',
    UNIQUE (member_id, class_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (class_id) REFERENCES fitness_classes(class_id)
);

CREATE TABLE personal_training_sessions (
    psession_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    trainer_id INT NOT NULL,
    room_id INT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'Scheduled',
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (trainer_id) REFERENCES trainers(trainer_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE swimming_sessions (
    swim_session_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    pool_id INT NOT NULL,
    trainer_id INT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'Scheduled',
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (pool_id) REFERENCES pools(pool_id),
    FOREIGN KEY (trainer_id) REFERENCES trainers(trainer_id)
);

CREATE TABLE physiotherapy_sessions (
    physio_session_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    physio_id INT NOT NULL,
    room_id INT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'Scheduled',
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (physio_id) REFERENCES physiotherapists(physio_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    member_membership_id INT NULL,
    psession_id INT NULL,
    swim_session_id INT NULL,
    physio_session_id INT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (member_membership_id) REFERENCES member_memberships(member_membership_id),
    FOREIGN KEY (psession_id) REFERENCES personal_training_sessions(psession_id),
    FOREIGN KEY (swim_session_id) REFERENCES swimming_sessions(swim_session_id),
    FOREIGN KEY (physio_session_id) REFERENCES physiotherapy_sessions(physio_session_id)
);

CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    attend_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(20) DEFAULT 'Present',
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);
USE aquafit_wellness_center;

INSERT INTO members (first_name, last_name, dob, email, phone, address) VALUES
('Ahmad', 'Khalil', '1990-05-15', 'ahmad.khalil@email.com', '0591234567', 'Nablus, Palestine'),
('Sara', 'Mahmoud', '1988-11-20', 'sara.mahmoud@email.com', '0599876543', 'Ramallah, Palestine'),
('Omar', 'Salem', '1995-07-10', 'omar.salem@email.com', '0595551234', 'Hebron, Palestine');

INSERT INTO membership_plans (name, duration_months, price) VALUES
('Basic Plan', 3, 150.00),
('Standard Plan', 6, 280.00),
('Premium Plan', 12, 500.00);

INSERT INTO trainers (first_name, last_name, specialty, email, phone) VALUES
('Yousef', 'Jaber', 'Yoga', 'yousef.jaber@email.com', '0591112222'),
('Lina', 'Hassan', 'Strength Training', 'lina.hassan@email.com', '0593334444');

INSERT INTO physiotherapists (first_name, last_name, specialty, email, phone) VALUES
('Rami', 'Nasser', 'Sports Injuries', 'rami.nasser@email.com', '0596667777');

INSERT INTO health_records (member_id, condition_name, notes, record_date) VALUES
(1, 'Back Pain', 'Needs special attention during lifting exercises', '2026-01-10'),
(2, 'Knee Injury', 'Avoid high impact workouts', '2026-02-15');

INSERT INTO pools (name, location, capacity) VALUES
('Main Pool', 'Ground Floor', 20),
('Therapy Pool', 'First Floor', 10);

INSERT INTO rooms (name, room_type, capacity) VALUES
('Yoga Room', 'Yoga', 15),
('Gym Room 1', 'Fitness', 20);

INSERT INTO schedules (day_of_week, start_time, end_time) VALUES
('Monday', '09:00:00', '10:00:00'),
('Wednesday', '17:00:00', '18:00:00');

INSERT INTO member_memberships (member_id, membership_id, start_date, end_date) VALUES
(1, 2, '2026-03-01', '2026-08-01'),
(2, 1, '2026-03-05', '2026-06-05');

INSERT INTO fitness_classes (class_name, schedule_id, room_id, trainer_id, max_capacity) VALUES
('Yoga Morning', 1, 1, 1, 15),
('Strength Training', 2, 2, 2, 20);

INSERT INTO class_enrollments (member_id, class_id, enrollment_date) VALUES
(1, 1, '2026-03-01'),
(2, 2, '2026-03-05');

INSERT INTO personal_training_sessions (member_id, trainer_id, room_id, session_date, start_time, end_time) VALUES
(1, 2, 2, '2026-03-10', '10:00:00', '11:00:00');

INSERT INTO swimming_sessions (member_id, pool_id, trainer_id, session_date, start_time, end_time) VALUES
(2, 1, 1, '2026-03-12', '15:00:00', '16:00:00');

INSERT INTO physiotherapy_sessions (member_id, physio_id, room_id, session_date, start_time, end_time) VALUES
(1, 1, 1, '2026-03-15', '11:00:00', '11:45:00');

INSERT INTO payments (member_id, member_membership_id, psession_id, swim_session_id, physio_session_id, amount, payment_date, payment_method) VALUES
(1, 1, 1, NULL, 1, 100.00, '2026-03-01', 'Cash'),
(2, 2, NULL, 1, NULL, 80.00, '2026-03-05', 'Card');

INSERT INTO attendance (member_id, attend_date, check_in_time, check_out_time) VALUES
(1, '2026-03-01', '08:55:00', '10:05:00'),
(2, '2026-03-05', '16:50:00', '18:10:00');