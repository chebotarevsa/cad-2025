CREATE TABLE IF NOT EXISTS CATEGORIES (
    category_id INT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS PRODUCTS (
    product_id     BIGINT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    description    VARCHAR(500),
    category_id    INT,
    price          DECIMAL(10, 2),
    stock_quantity INT,
    image_url      VARCHAR(500),
    created_at     DATE,
    updated_at     DATE,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
);
