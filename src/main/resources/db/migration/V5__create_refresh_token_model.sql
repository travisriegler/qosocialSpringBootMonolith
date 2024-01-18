CREATE TABLE refresh_token_model (
    token_id VARCHAR(255) PRIMARY KEY,
    expiration_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES app_user_model (user_id)
        ON DELETE CASCADE
);