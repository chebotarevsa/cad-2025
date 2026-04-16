-- Таблица категорий
CREATE TABLE CATEGORIES
(
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(1024)
);

-- Таблица товаров
CREATE TABLE PRODUCTS
(
    product_id     INT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(255)   NOT NULL,
    description    VARCHAR(2048),
    category_id    INT            NOT NULL,
    price          DECIMAL(10, 2) NOT NULL,
    stock_quantity INT            NOT NULL,
    image_url      VARCHAR(1024),
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Внешний ключ на таблицу категорий
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
            REFERENCES CATEGORIES (category_id)
            ON DELETE RESTRICT
);