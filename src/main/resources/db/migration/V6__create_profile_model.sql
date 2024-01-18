CREATE TABLE profile_model (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE CHECK (username <> '' AND username NOT LIKE '% %'),
    bio TEXT,
    picture_url VARCHAR(1024),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP NULL DEFAULT NULL,

    CONSTRAINT fk_user2
        FOREIGN KEY (user_id)
        REFERENCES app_user_model (user_id)
        ON DELETE CASCADE
);