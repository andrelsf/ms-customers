-- INSERTS Customers
INSERT INTO
    customers(customer_id, name, cpf)
VALUES
    ('3d05773e-513e-11ef-85b4-938a0beed59a', 'Alice Bar', '22233344448'),
    ('5ccac7a4-513e-11ef-9485-bbfbb6bdc7c6', 'Bob Foo', '33344455587');

-- INSERTS Accounts
INSERT INTO
    accounts(account_id, agency, account_number, status, balance)
VALUES
    ('3d05773e-513e-11ef-85b4-938a0beed59a', 1234, 1000112, 'ACTIVE', 10000.00),
    ('5ccac7a4-513e-11ef-9485-bbfbb6bdc7c6', 1234, 1000223, 'ACTIVE', 10000.00);