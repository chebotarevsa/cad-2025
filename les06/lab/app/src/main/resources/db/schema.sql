CREATE TABLE CATEGORIES (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)

);

CREATE TABLE PRODUCTS (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    category_id INT,
    price DECFLOAT(20),
    stock_quantity INT,
    image_url VARCHAR(255),
    created_at DATE,
    updated_at DATE,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(id)
);