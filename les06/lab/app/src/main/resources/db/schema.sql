CREATE TABLE Categories(
    id identity primary key,
    name varchar(255) not null,
    description varchar(255) not null
);

CREATE TABLE Products(
    id identity primary key,
    name varchar(255) not null,
    description varchar(255) not null,
    category_id int,
    FOREIGN KEY (category_id) REFERENCES Categories(id),
    price decimal,
    stock_quantity int,
    image_url varchar,
    created_at datetime,
    updated_at datetime
);