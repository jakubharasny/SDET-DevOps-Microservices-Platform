CREATE TABLE IF NOT EXISTS query_request (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    message VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL,
    result_text VARCHAR(500) NULL,
    error_text VARCHAR(500) NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
