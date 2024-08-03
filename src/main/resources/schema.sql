-- Create Table Clients
CREATE TABLE accounts (
    account_id     VARCHAR(36)       NOT NULL PRIMARY KEY,
    customer_id    VARCHAR(36)       UNIQUE NOT NULL,
    agency         INTEGER           NOT NULL,
    account_number INTEGER           UNIQUE NOT NULL,
    status         VARCHAR(9)        NOT NULL,
    balance        DECIMAL(12,2)     NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_accounts
ON accounts (account_id, customer_id, agency, account_number, status, created_at);

-- Create Table Transactions
CREATE TABLE transactions (
    transaction_id        VARCHAR(36)       NOT NULL PRIMARY KEY,
    customer_id           VARCHAR(36)       NOT NULL,
    target_agency         INTEGER           NOT NULL,
    target_account_number INTEGER           NOT NULL,
    amount                DECIMAL(12, 2)    NOT NULL,
    status                VARCHAR(9)        NOT NULL, -- COMPLETED, FAILED
    comment               VARCHAR(120)      NOT NULL,
    transfer_date         TIMESTAMP         DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions
ON transactions (transaction_id, customer_id, status);