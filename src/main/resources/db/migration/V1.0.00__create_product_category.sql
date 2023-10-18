BEGIN;

CREATE TABLE IF NOT EXISTS product
(
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(255)   NOT NULL,
    description        TEXT,
    price              NUMERIC(19, 4) NOT NULL,
    category_id        BIGINT,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS category
(
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);

COMMIT;