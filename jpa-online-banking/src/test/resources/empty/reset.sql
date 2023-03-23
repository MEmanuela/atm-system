
SELECT setval('role_id_seq', (select max(id) from role));
SELECT setval('account_type_id_seq', (select max(id) from account_type));
SELECT setval('transaction_type_id_seq', (select max(id) from transaction_type));
SELECT setval('user__id_seq', (select max(id) from user_));
SELECT setval('account_id_seq', (select max(id) from account));
SELECT setval('transaction_id_seq', (select max(id) from transaction));