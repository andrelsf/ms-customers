-- Create Table Clients
CREATE TABLE accounts (
    account_id     VARCHAR(36)       NOT NULL PRIMARY KEY,
    agency         INTEGER           NOT NULL,
    account_number INTEGER           UNIQUE NOT NULL,
    status         VARCHAR(9)        NOT NULL,
    balance        DECIMAL(12,2)     NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_accounts
ON accounts (account_id, agency, account_number, status, created_at);

-- Create Table Transfers
CREATE TABLE transfers (
    transfer_id           VARCHAR(36)    NOT NULL PRIMARY KEY,
    account_id            VARCHAR(36)    NOT NULL,
    target_agency         INTEGER        NOT NULL,
    target_account_number INTEGER        NOT NULL,
    amount                DECIMAL(12, 2) NOT NULL,
    status                VARCHAR(9)     NOT NULL,
    message               VARCHAR(180)   NOT NULL,
    transfer_date         TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transfers
ON transfers (transfer_id, account_id, status);

-- Create Table transactions
CREATE TABLE transactions (
    transaction_id  VARCHAR(36) NOT NULL PRIMARY KEY
);