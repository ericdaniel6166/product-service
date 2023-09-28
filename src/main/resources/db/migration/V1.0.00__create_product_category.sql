BEGIN;

CREATE TABLE IF NOT EXISTS product
(
    id                 bigserial PRIMARY KEY,
    name               VARCHAR(255)   NOT NULL,
    description        TEXT,
    price              numeric(19, 4) NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS category
(
    id                 bigserial PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS product_category
(
    id                 bigserial PRIMARY KEY,
    product_id         INT REFERENCES product (id),
    category_id        INT REFERENCES category (id),
    created_by         VARCHAR(255),
    created_date       TIMESTAMP(6),
    last_modified_by   VARCHAR(255),
    last_modified_date TIMESTAMP(6)
);

COMMIT;