

INSERT INTO product (id, title, img_path, price, description)
VALUES (100, 'Тестовый товар',  '/images/test.png', 150.00, 'Описание');

INSERT INTO cart (id, product_id)
VALUES (200, 100),
       (201, 100);

