CREATE TABLE post_model (
    post_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    text_content TEXT,
    media_url VARCHAR(1024),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP NULL DEFAULT NULL,
    profile_id BIGINT NOT NULL,

    CONSTRAINT fk_profile
        FOREIGN KEY (profile_id)
        REFERENCES profile_model (profile_id)
        ON DELETE CASCADE
);