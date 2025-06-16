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
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(id),
    price DECFLOAT(20),
    stock_quantity INT,
    image_url VARCHAR(255),
    created_at DATE,
    updated_at DATE
    
);