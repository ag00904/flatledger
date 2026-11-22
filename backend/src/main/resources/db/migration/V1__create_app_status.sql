-- One-row table that /api/ping reads, proving page -> API -> database works.
CREATE TABLE app_status (
    id     SMALLINT    PRIMARY KEY CHECK (id = 1),
    status VARCHAR(20) NOT NULL
);

INSERT INTO app_status (id, status) VALUES (1, 'ok');
