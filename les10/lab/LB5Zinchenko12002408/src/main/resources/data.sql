
INSERT INTO CATEGORIES (category_id, name, description) VALUES (1, 'Корма', 'Сухие и влажные корма');
INSERT INTO CATEGORIES (category_id, name, description) VALUES (2, 'Игрушки', 'Игрушки для животных');


INSERT INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at)
VALUES (1, 'Корм для кошек', 'Вкусный корм', 1, 500.00, 50, 'http://img1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO PRODUCTS (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at)
VALUES (2, 'Мячик', 'Резиновый мяч', 2, 150.00, 20, 'http://img2.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CUSTOMERS (customer_id, name, email, phone, address) VALUES (1, 'Иван Иванов', 'ivan@mail.ru', '+79991112233', 'г. Москва, ул. Ленина, д. 1');