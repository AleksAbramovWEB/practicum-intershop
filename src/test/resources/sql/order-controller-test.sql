INSERT INTO product (id, title, img_path, price, description)
VALUES (200, 'Продукт для заказа', '/images/test.png',500.00, 'Описание товара');

INSERT INTO cart (id, product_id)
VALUES (1000, 200);

INSERT INTO orders (id, total_sum) VALUES (300, 1000.00);

INSERT INTO order_item (id, order_id, product_id, count, total_sum) VALUES (400, 300, 200, 2, 1000.00);



