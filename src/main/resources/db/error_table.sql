CREATE TABLE IF NOT EXISTS error_messages
(
    id               BIGSERIAL PRIMARY KEY,
    original_message TEXT,
    error_type       VARCHAR(255) NOT NULL,
    error_message    TEXT         NOT NULL,
    queue            VARCHAR(255) NOT NULL,
    retries          INT          NOT NULL,
    message_id       VARCHAR(255) NOT NULL,
    timestamp        TIMESTAMP    NOT NULL
);