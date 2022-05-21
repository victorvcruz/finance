CREATE TABLE account (
	id VARCHAR PRIMARY KEY,
	username VARCHAR UNIQUE NOT NULL,
	password VARCHAR NOT NULL,
	created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL
);

CREATE TABLE transaction (
	id VARCHAR PRIMARY KEY,
	account_id VARCHAR NOT NULL,
	category_id VARCHAR NOT NULL,
	description VARCHAR NOT NULL,
	value REAL NOT NULL,
	date TIMESTAMP NOT NULL,
	canceled BOOLEAN NOT NULL,
	created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL
);

CREATE TABLE category (
	id VARCHAR PRIMARY KEY,
	name VARCHAR NOT NULL,
	account_id VARCHAR NOT NULL,
	created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL
);
