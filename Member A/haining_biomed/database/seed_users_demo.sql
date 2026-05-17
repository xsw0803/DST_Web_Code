USE week21_db;

INSERT INTO users(username, password, role, display_name, created_at)
VALUES ('patient_demo', '123456', 'patient', 'Patient Demo', NOW()),
       ('doctor_demo', '123456', 'professional', 'Professional Demo', NOW()),
       ('admin_demo', '123456', 'admin', 'Admin Demo', NOW())
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    role = VALUES(role),
    display_name = VALUES(display_name);
