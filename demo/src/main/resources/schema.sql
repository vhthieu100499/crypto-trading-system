
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(50) NOT NULL,
  balance DOUBLE NOT NULL DEFAULT 50000,
  CONSTRAINT users_userName_UNIQUE UNIQUE (user_name));

CREATE TABLE trading_ticket (
  id INT AUTO_INCREMENT PRIMARY KEY,
  symbol VARCHAR(20) NOT NULL,
  ask_price DOUBLE NOT NULL,
  bid_price DOUBLE NOT NULL,
  is_available BIT DEFAULT 1,
  created_date TIMESTAMP);
  
CREATE TABLE trading (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  trading_ticket_id INT NOT NULL);

CREATE TABLE trading_history (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  trading_ticket_id INT NOT NULL,
  trading_date TIMESTAMP,
  trading_type BIT);

  