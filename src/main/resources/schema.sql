CREATE TABLE IF NOT EXISTS user (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    problems_solved INT NOT NULL,
    rank INT NOT NULL,
    contests_attended INT NOT NULL
);