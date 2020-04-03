CREATE TABLE IF NOT EXISTS halls (
    id SERIAL PRIMARY KEY,
    hallid INT,
    row INT,
    place INT,
    state BOOL,
    accountid BIGINT
);
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY,
    surname VARCHAR (50),
    name VARCHAR (50),
    middleName VARCHAR (50)
);