# Отчет о лаботаротоной работе

## Цель работы
Научиться использовать Hibernate ORM и Spring Data JPA в Java-приложении, реализовать многослойную архитектуру, связать сущности и настроить автоматическое создание схемы базы данных. Расширить проект логикой создания заказов с транзакционной поддержкой.

## Выполнение работы
1. Создание проекта
Проект был создан с помощью команды:
gradle init --package ru.bsuedu.cad.lab

2. Настройка DataSource
Использована встроенная база данных H2.

Применён пул соединений HikariCP.

Добавлены зависимости:

implementation("org.springframework.boot:spring-boot-starter-data-jpa")
implementation("com.zaxxer:HikariCP")
implementation("com.h2database:h2")

3. Структура пакетов проекта

ru.bsuedu.cad.lab
│
├── entity        // JPA сущности
├── repository    // интерфейсы Spring Data
├── service       // бизнес-логика
└── app           // точка входа + конфигурация

4. Создание JPA сущностей
    Созданы классы:
    Category
    Product
    Customer
    CustomerOrder
    OrderDetail

Они соответствуют следующей диаграмме:

erDiagram
    CATEGORIES ||--o{ PRODUCTS : содержит
    CUSTOMERS ||--o{ ORDERS : размещает
    ORDERS ||--o{ ORDER_DETAILS : содержит
    PRODUCTS ||--o{ ORDER_DETAILS : включен в

    CATEGORIES {
        int category_id PK
        string name
        string description
    }

    PRODUCTS {
        int product_id PK
        string name
        string description
        int category_id FK
        decimal price
        int stock_quantity
        string image_url
        datetime created_at
        datetime updated_at
    }

    CUSTOMERS {
        int customer_id PK
        string name
        string email
        string phone
        string address
    }

    ORDERS {
        int order_id PK
        int customer_id FK
        datetime order_date
        decimal total_price
        string status
        string shipping_address
    }

    ORDER_DETAILS {
        int order_detail_id PK
        int order_id FK
        int product_id FK
        int quantity
        decimal price
    }
5. Реализация репозиториев
Для каждой сущности созданы репозитории, расширяющие JpaRepository. Примеры:

public interface ProductRepository extends JpaRepository<Product, Integer> {}
public interface CustomerRepository extends JpaRepository<Customer, Integer> {}

6. Реализация сервисов
Создан сервис OrderService с методом createOrder(...), который:
    Создаёт заказ
    Заполняет детали заказа
    Поддерживает транзакции

7. Загрузка данных из CSV
Создан компонент CsvDataLoader, который:
    Загружает категории, клиентов, товары из *.csv
    Сохраняет в базу перед запуском основного приложения
    Использует библиотеку OpenCSV

8. Точка входа — App.java
В main():
    Создаётся Spring-контекст
    Получаются репозитории и сервисы
    Создаётся заказ
    Логируется успешное завершение

9. UML-диаграмму классов 
classDiagram
    class Category {
        int id
        String name
        String description
    }

    class Product {
        int id
        String name
        String description
        BigDecimal price
        int stockQuantity
        String imageUrl
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    class Customer {
        int id
        String name
        String email
        String phone
        String address
    }

    class CustomerOrder {
        int id
        LocalDateTime orderDate
        BigDecimal totalPrice
        String status
        String shippingAddress
    }

    class OrderDetail {
        int id
        int quantity
        BigDecimal price
    }

    Category "1" --> "many" Product : содержит
    Customer "1" --> "many" CustomerOrder : оформляет
    CustomerOrder "1" --> "many" OrderDetail : содержит
    Product "1" --> "many" OrderDetail : участвует
    Product --> Category
    CustomerOrder --> Customer
    OrderDetail --> Product
    OrderDetail --> CustomerOrder

## Выводы
В рамках лабораторной работы: 
    Освоены основы Hibernate и Spring Data JPA
    Реализована многослойная архитектура приложения
    Осуществлена загрузка и связка сущностей
    Заказы формируются и сохраняются с транзакционной поддержкой
    Приложение корректно работает с автоматическим созданием схемы
