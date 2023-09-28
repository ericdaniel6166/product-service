BEGIN;


INSERT INTO category (name, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('Electronics', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Clothing', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Home & Garden', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Toys & Games', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Sports & Outdoors', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Books', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Furniture', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Jewelry', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Health & Beauty', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW()),
       ('Food', 'product-service-v1.0', NOW(), 'product-service-v1.0', NOW());


CREATE TEMP TABLE if not exists category_assignments AS
SELECT p.product_id,
       CASE
           WHEN p.product_id % 10 = 1 THEN 1
           WHEN p.product_id % 10 = 2 THEN 2
           WHEN p.product_id % 10 = 3 THEN 3
           WHEN p.product_id % 10 = 4 THEN 4
           WHEN p.product_id % 10 = 5 THEN 5
           WHEN p.product_id % 10 = 6 THEN 6
           WHEN p.product_id % 10 = 7 THEN 7
           WHEN p.product_id % 10 = 8 THEN 8
           WHEN p.product_id % 10 = 9 THEN 9
           WHEN p.product_id % 10 = 0 THEN 10
           END                                              AS category_id,
       CASE
           WHEN p.product_id % 10 = 1 THEN 'iPhone'
           WHEN p.product_id % 10 = 2 THEN 'Designer Jeans'
           WHEN p.product_id % 10 = 3 THEN 'LED TV'
           WHEN p.product_id % 10 = 4 THEN 'Toy Train Set'
           WHEN p.product_id % 10 = 5 THEN 'Football'
           WHEN p.product_id % 10 = 6 THEN 'Mystery Novel'
           WHEN p.product_id % 10 = 7 THEN 'Coffee Table'
           WHEN p.product_id % 10 = 8 THEN 'Diamond Necklace'
           WHEN p.product_id % 10 = 9 THEN 'Supplement'
           WHEN p.product_id % 10 = 0 THEN 'Beef'
           END || ' ' || (floor(random() * 10000)::integer) AS product_name
FROM generate_series(1, 150) AS p(product_id);


INSERT INTO product (name, description, price, created_by, created_date, last_modified_by, last_modified_date)
SELECT ca.product_name,
       'Description for ' || ca.product_name,
       (random() * 1000)::numeric(19, 4),
       'product-service-v1.0',
       NOW(),
       'product-service-v1.0',
       NOW()
FROM category_assignments ca;


INSERT INTO product_category (product_id, category_id, created_by, created_date, last_modified_by, last_modified_date)
SELECT ca.product_id,
       ca.category_id,
       'product-service-v1.0',
       NOW(),
       'product-service-v1.0',
       NOW()
FROM category_assignments ca;


DROP TABLE if exists category_assignments;

COMMIT;
