CREATE TABLE IF NOT EXISTS CATEGORIES (
                                          category_id INTEGER PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL,
    description VARCHAR(500)
    );

CREATE TABLE IF NOT EXISTS PRODUCTS (
                                        product_id INTEGER PRIMARY KEY,
                                        name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    category_id INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    image_url VARCHAR(500),
    created_at DATE,
    updated_at DATE,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_product_category ON PRODUCTS(category_id);
CREATE INDEX IF NOT EXISTS idx_product_price ON PRODUCTS(price);