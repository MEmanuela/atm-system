CREATE TABLE IF NOT EXISTS role (
    id  BIGINT NOT NULL AUTO_INCREMENT,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS user (
    id  BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    personal_code_number VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);
CREATE TABLE IF NOT EXISTS account_type (
    id  BIGINT NOT NULL AUTO_INCREMENT,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS account (
    id  BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date_opened DATE NOT NULL,
    balance FLOAT NOT NULL,
    type_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (type_id) REFERENCES account_type(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);
CREATE TABLE IF NOT EXISTS transaction_type (
    id  BIGINT NOT NULL AUTO_INCREMENT,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS transaction (
    id  BIGINT NOT NULL AUTO_INCREMENT,
    date DATE NOT NULL,
    amount FLOAT NOT NULL,
    base_account_id BIGINT NOT NULL,
    receiving_account_id BIGINT DEFAULT NULL,
    type_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (base_account_id) REFERENCES account(id),
    FOREIGN KEY (receiving_account_id) REFERENCES account(id),
    FOREIGN KEY (type_id) REFERENCES transaction_type(id)
);
CREATE TABLE IF NOT EXISTS token (
    id  BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(255) NOT NULL,
    is_expired BOOLEAN NOT NULL,
    is_revoked BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    UNIQUE(token)
);