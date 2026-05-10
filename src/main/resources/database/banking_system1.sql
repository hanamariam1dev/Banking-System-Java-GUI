
CREATE DATABASE IF NOT EXISTS banking_system1;
USE banking_system1;


CREATE TABLE IF NOT EXISTS accounts (
    account_id          INT AUTO_INCREMENT PRIMARY KEY,
    account_number      VARCHAR(20)  UNIQUE NOT NULL,
    account_holder_name VARCHAR(100) NOT NULL,
    email               VARCHAR(100),
    phone               VARCHAR(15),
    username            VARCHAR(50)  UNIQUE NOT NULL,
    password            VARCHAR(100) NOT NULL,
    role                ENUM('Customer','Admin') DEFAULT 'Customer',
    account_type        ENUM('Savings','Current','Fixed Deposit') DEFAULT 'Savings',
    balance             DECIMAL(15,2) DEFAULT 0.00,
    status              ENUM('Active','Inactive','Frozen') DEFAULT 'Active',
    profile_image       VARCHAR(500),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


ALTER TABLE accounts ADD COLUMN IF NOT EXISTS role          ENUM('Customer','Admin') DEFAULT 'Customer';
ALTER TABLE accounts ADD COLUMN IF NOT EXISTS profile_image VARCHAR(500);


CREATE TABLE IF NOT EXISTS transactions (
    transaction_id    INT AUTO_INCREMENT PRIMARY KEY,
    account_number    VARCHAR(20) NOT NULL,
    transaction_type  ENUM('Deposit','Withdrawal','Transfer Out','Transfer In') NOT NULL,
    amount            DECIMAL(15,2) NOT NULL,
    balance_after     DECIMAL(15,2) NOT NULL,
    recipient_account VARCHAR(20),
    description       VARCHAR(255),
    transaction_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
─
INSERT IGNORE INTO accounts
    (account_number, account_holder_name, email, phone, username, password, role, account_type, balance)
VALUES
    ('CBE1234567890', 'Admin User', 'admin@cbe.com', '0911000000', 'admin', 'admin123', 'Admin', 'Savings', 0.00);

UPDATE accounts SET role = 'Admin' WHERE username = 'admin';

SELECT 'Database setup complete!' AS Status;
