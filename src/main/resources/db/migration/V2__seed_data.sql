INSERT INTO sedes (id, name, address, city, phone) VALUES
(1, 'MediSalud Sede Norte', 'Calle 100 #15-20', 'Bogota', '6011234567'),
(2, 'MediSalud Sede Centro', 'Carrera 7 #32-10', 'Bogota', '6012345678'),
(3, 'MediSalud Sede Sur', 'Autopista Sur #45-60', 'Bogota', '6013456789');

INSERT INTO doctors (id, name, specialty, phone, email) VALUES
(1, 'Dra. Maria Gonzalez', 'Cardiologia', '5551001', 'maria.gonzalez@medisalud.com'),
(2, 'Dr. Carlos Ruiz', 'Pediatria', '5551002', 'carlos.ruiz@medisalud.com'),
(3, 'Dra. Ana Lopez', 'Dermatologia', '5551003', 'ana.lopez@medisalud.com');

INSERT INTO doctor_sedes (doctor_id, sede_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 3),
(3, 2);

INSERT INTO patients (id, name, document_id, phone, email, birth_date) VALUES
(1, 'Juan Perez', '1234567890', '3001112233', 'juan.perez@email.com', '1990-05-15'),
(2, 'Ana Martinez', '0987654321', '3004445566', 'ana.martinez@email.com', '1985-11-20'),
(3, 'Carlos Gomez', '1122334455', '3007778899', 'carlos.gomez@email.com', '2000-03-08');

SELECT setval('sedes_id_seq', 3);
SELECT setval('doctors_id_seq', 3);
SELECT setval('patients_id_seq', 3);
