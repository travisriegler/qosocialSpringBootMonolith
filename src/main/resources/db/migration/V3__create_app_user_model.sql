CREATE TABLE app_user_model (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE CHECK (email <> '' AND email NOT LIKE '% %'),
    password VARCHAR(255) NOT NULL CHECK (password <> '' AND password NOT LIKE '% %'),
    accepted_terms BOOLEAN NOT NULL DEFAULT FALSE,
    account_non_expired BOOLEAN NOT NULL DEFAULT FALSE,
    account_non_locked BOOLEAN NOT NULL DEFAULT FALSE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT FALSE,
    enabled BOOLEAN NOT NULL DEFAULT FALSE
);