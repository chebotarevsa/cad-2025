CREATE TABLE Categories (
    category_id  identity primary key,
    name varchar (255) not null,
    description varchar (255) not null
);

CREATE TABLE Products(
    product_id  identity primary key,
    name varchar (255) not null,
    description varchar (255) not null,
    category_id int,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    price decimal,
    stock_quantity int,
    image_url varchar,
    created_at datetime,
    updated_at datetime
);