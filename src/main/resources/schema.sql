DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS refresh_tokens;

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

CREATE TABLE refresh_tokens
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    refresh_token VARCHAR(500) NOT NULL UNIQUE,
    expires_at    TIMESTAMP    NOT NULL
);