INSERT INTO role (id, type) VALUES (1, 'admin');
INSERT INTO role (id, type) VALUES (2, 'customer');

INSERT INTO user (id, name, phone, email, personal_code_number, username, password, role_id)
VALUES (1, 'John Doe', '0856695427', 'john.doe@gmail.com', '9056884239702', 'john_doe', 'admin',1);
INSERT INTO user (id, name, phone, email, personal_code_number, username, password, role_id)
VALUES (2, 'Paul Hewson', '0859986521', 'paul.hewson@gmail.com', '8965223841087', 'paul_hewson', 'password', 2);
INSERT INTO user (id, name, phone, email, personal_code_number, username, password, role_id)
VALUES (3, 'Adam Clayton', '0858856237', 'adam.clayton@gmail.com', '9085447123690', 'adam_clayton', 'password' ,2);

INSERT INTO account_type (id, type)
VALUES (1, 'basic');
INSERT INTO account_type (id, type)
VALUES (2, 'saving');
INSERT INTO account_type (id, type)
VALUES (3, 'fixed deposit');

INSERT INTO account (id, name, date_opened, type_id, user_id)
VALUES (1, 'Adam Clayton', '1999-06-10', 1, 1);
INSERT INTO account (id, name, date_opened, type_id, user_id)
VALUES (2, 'Paul Hewson', '1999-03-13', 2, 2);

INSERT INTO transaction_type(id, type)
VALUES (1, 'withdraw');
INSERT INTO transaction_type(id, type)
VALUES (2, 'deposit');
INSERT INTO transaction_type(id, type)
VALUES (3, 'transfer');

INSERT INTO transaction(id, date, amount, base_account_id, receiving_account_id, type_id)
VALUES (1, '2019-12-12', 50.0, 1, 1, 1);
INSERT INTO transaction(id, date, amount, base_account_id, receiving_account_id, type_id)
VALUES (2, '2019-12-12', 50.0, 1, 1, 2);
INSERT INTO transaction(id, date, amount, base_account_id, receiving_account_id, type_id)
VALUES (3, '2019-12-12', 50.0, 1, 2, 3);