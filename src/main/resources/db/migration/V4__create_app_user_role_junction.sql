CREATE TABLE app_user_role_junction (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES app_user_model(user_id),
    FOREIGN KEY (role_id) REFERENCES role_model(role_id)
);