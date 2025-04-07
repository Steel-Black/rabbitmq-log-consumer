CREATE TABLE IF NOT EXISTS log_record
(
    id             BIGSERIAL PRIMARY KEY,
    message        TEXT,
    step           VARCHAR(255) NOT NULL,
    logger         VARCHAR(255) NOT NULL,
    host           VARCHAR(255) NOT NULL,
    correlation_id VARCHAR(255) NOT NULL,
    level          VARCHAR(50)  NOT NULL,
    method         VARCHAR(255) NOT NULL,
    time_stamp      TIMESTAMP    NOT NULL
);