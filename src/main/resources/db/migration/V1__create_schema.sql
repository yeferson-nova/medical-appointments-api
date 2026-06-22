CREATE TABLE sedes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE doctor_sedes (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL REFERENCES doctors(id),
    sede_id BIGINT NOT NULL REFERENCES sedes(id),
    UNIQUE(doctor_id, sede_id)
);

CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    document_id VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    birth_date DATE
);

CREATE TABLE inventory_slots (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL REFERENCES doctors(id),
    sede_id BIGINT NOT NULL REFERENCES sedes(id),
    slot_date DATE NOT NULL,
    time_start TIME NOT NULL,
    time_end TIME NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT NOT NULL DEFAULT 0,
    UNIQUE(doctor_id, sede_id, slot_date, time_start)
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL REFERENCES doctors(id),
    patient_id BIGINT NOT NULL REFERENCES patients(id),
    sede_id BIGINT NOT NULL REFERENCES sedes(id),
    slot_id BIGINT REFERENCES inventory_slots(id),
    date_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',
    cancellation_timestamp TIMESTAMP,
    penalty_applied BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX uk_doctor_sede_slot_active
    ON appointments(doctor_id, sede_id, date_time)
    WHERE status != 'CANCELADA';

CREATE TABLE penalties (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id),
    appointment_id BIGINT NOT NULL REFERENCES appointments(id),
    penalty_date TIMESTAMP NOT NULL
);

CREATE INDEX idx_appointments_doctor_sede_date ON appointments(doctor_id, sede_id, date_time);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_status ON appointments(status);
CREATE INDEX idx_inventory_slots_available ON inventory_slots(doctor_id, sede_id, slot_date, is_available);
CREATE INDEX idx_penalties_patient_date ON penalties(patient_id, penalty_date);
