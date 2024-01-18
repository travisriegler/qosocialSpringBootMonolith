CREATE TABLE role_model (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE CHECK (name <> '' AND name NOT LIKE '% %')
);
