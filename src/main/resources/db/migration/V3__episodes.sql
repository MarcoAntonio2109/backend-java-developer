-- Criação da tabela episodes com validação se já existe
CREATE TABLE IF NOT EXISTS episodes (
    id BIGINT PRIMARY KEY,
    url VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    season INT,
    number INT,
    type VARCHAR(255),
    airdate VARCHAR(255),
    airtime VARCHAR(255),
    airstamp VARCHAR(255),
    runtime INT,
    average DOUBLE PRECISION,
    image VARCHAR(255),
    summary TEXT,
    self_href VARCHAR(255),
    show_href VARCHAR(255),
    show_name VARCHAR(255)
);