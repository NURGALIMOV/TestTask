CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    description VARCHAR(2000),
    created TIMESTAMP,
    done BOOL
);