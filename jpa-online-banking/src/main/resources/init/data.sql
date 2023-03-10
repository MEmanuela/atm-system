INSERT INTO role (id, type) VALUES (1, 'admin');
INSERT INTO role (id, type) VALUES (2, 'customer');

INSERT INTO account_type (id, type) VALUES (1, 'basic');
INSERT INTO account_type (id, type) VALUES (2, 'saving');
INSERT INTO account_type (id, type) VALUES (3, 'fixed deposit');

INSERT INTO transaction_type(id, type) VALUES (1, 'withdraw');
INSERT INTO transaction_type(id, type) VALUES (2, 'deposit');
INSERT INTO transaction_type(id, type) VALUES (3, 'transfer');

INSERT INTO user (id, name, phone, email, personal_code_number, username, password, role_id)
VALUES (1, 'Paul Hewson', '0859986521', 'paul.hewson@gmail.com', '8965223841087', 'paul_hewson', '$2a$10$CDzNYTgimt7von8IRlsU2eWIfnxt/IuoNksu/qZih.mTU4HOZggzS', 2);
INSERT INTO user (id, name, phone, email, personal_code_number, username, password, role_id)
VALUES (2, 'Adam Clayton', '0858856237', 'adam.clayton@gmail.com', '9085447123690', 'adam_clayton', '$2a$10$/PacO64PwJeBK82LdeZxoO3QbysaV7SLWQD4TJk7bp0yJSR2BJWc2' ,2);
INSERT INTO user (id, name, phone, email, personal_code_number, username, password, role_id)
VALUES (3, 'Alex Smith', '0956695231', 'alex.smith@gmail.com', '9539486851785', 'alex_smith', '$2a$10$wnVs14z7Qc8JHvdrsxYihukN5C0mUiUp4dcwR6o.Iche50Tc9Q5dm' ,2);
INSERT INTO user (id, name, phone, email, personal_code_number, username, password, role_id)
VALUES (4, 'Criss Evans', '0858856237', 'criss.evans@gmail.com', '9539496851785', 'criss_evans', '$2a$10$Z7s6/lojv/u/tqJ15qU8duw9gKKAhrN9WA24X48CPbV/AteMxx/EK' ,1);

INSERT INTO account (id, name, date_opened, balance, type_id, user_id)
VALUES (1, 'PaulHewson', '2019-12-12', 0.0,  1, 1);
INSERT INTO account (id, name, date_opened, balance, type_id, user_id)
VALUES (2, 'PaulHewson', '2019-12-12', 0.0,  2, 1);
INSERT INTO account (id, name, date_opened, balance, type_id, user_id)
VALUES (3, 'AdamClayton', '2019-12-12', 0.0, 1, 2);
INSERT INTO account (id, name, date_opened, balance, type_id, user_id)
VALUES (4, 'AdamClayton', '2019-12-12', 0.0, 2, 2);
INSERT INTO account (id, name, date_opened, balance, type_id, user_id)
VALUES (5, 'AlexSmith', '2019-12-12', 0.0, 1, 3);
INSERT INTO account (id, name, date_opened, balance, type_id, user_id)
VALUES (6, 'AlexSmith', '2019-12-12', 0.0, 2, 3);