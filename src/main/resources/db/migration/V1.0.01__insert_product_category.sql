BEGIN;


INSERT INTO category (name, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('Electronics', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Clothing', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Home & Garden', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Toys & Games', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Sports & Outdoors', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Books', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Furniture', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Jewelry', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Health & Beauty', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year'),
       ('Food', 'product-service-v1.0', now() - INTERVAL '8 year' + random() * INTERVAL '1 year',
        'product-service-v1.0', now() - INTERVAL '6 year' + random() * INTERVAL '1 year');


CREATE TEMP TABLE IF NOT EXISTS category_assignments AS
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
           END                               AS category_id,
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
           END || ' ' || (gen_random_uuid()) AS product_name
FROM generate_series(1, 150) AS p(product_id);


INSERT INTO product (name, description, price, created_by, created_date, last_modified_by, last_modified_date,
                     category_id)
SELECT ca.product_name,
       'Description for ' || ca.product_name,
       (random() * 1000)::NUMERIC(19, 4),
       'product-service-v1.0',
       now() - INTERVAL '4 year' + random() * INTERVAL '1 year',
       'product-service-v1.0',
       now() - INTERVAL '2 year' + random() * INTERVAL '1 year',
       random() * 9 + 1
FROM category_assignments ca;


DROP TABLE IF EXISTS category_assignments;

COMMIT;
