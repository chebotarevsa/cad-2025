# Лабораторная работа 4

Тема: JPA, Hibernate, Spring Data.

В этой работе было сделано приложение для магазина зоотоваров. Старый подход с JDBC заменен на JPA/Hibernate и Spring Data.

## Что сделано

1. Создан проект в директории les08/lab.
2. Настроена база данных H2.
3. Для подключения к базе используется HikariDataSource.
4. Hibernate сам создает таблицы по JPA-сущностям.
5. Созданы сущности:
   - Category
   - Product
   - Customer
   - Order
   - OrderDetail
6. Для каждой сущности создан Spring Data репозиторий.
7. Создан сервис OrderService, который умеет создавать заказ и получать список заказов.
8. В приложении загружаются данные из CSV-файлов и создается новый заказ.

## Структура пакетов

ru.bsuedu.cad.lab
ru.bsuedu.cad.lab.entity
ru.bsuedu.cad.lab.repository
ru.bsuedu.cad.lab.service
ru.bsuedu.cad.lab.app

## UML-диаграмма классов

```mermaid
classDiagram
    class Category {
        -Integer id
        -String name
        -String description
        -List~Product~ products
    }

    class Product {
        -Integer id
        -String name
        -String description
        -Category category
        -BigDecimal price
        -Integer stockQuantity
        -String imageUrl
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }

    class Customer {
        -Integer id
        -String name
        -String email
        -String phone
        -String address
        -List~Order~ orders
    }

    class Order {
        -Integer id
        -Customer customer
        -LocalDateTime orderDate
        -BigDecimal totalPrice
        -String status
        -String shippingAddress
        -List~OrderDetail~ details
        +addDetail(OrderDetail detail)
    }

    class OrderDetail {
        -Integer id
        -Order order
        -Product product
        -Integer quantity
        -BigDecimal price
    }

    class CategoryRepository
    class ProductRepository
    class CustomerRepository
    class OrderRepository
    class OrderDetailRepository

    class OrderItem {
        -Integer productId
        -Integer quantity
    }

    class OrderService {
        +createOrder(Integer customerId, List~OrderItem~ items, String shippingAddress) Order
        +findAllOrders() List~Order~
    }

    class App {
        +main(String[] args)
    }

    Category "1" --> "*" Product
    Customer "1" --> "*" Order
    Order "1" --> "*" OrderDetail
    Product "1" --> "*" OrderDetail

    CategoryRepository ..> Category
    ProductRepository ..> Product
    CustomerRepository ..> Customer
    OrderRepository ..> Order
    OrderDetailRepository ..> OrderDetail

    OrderService ..> CustomerRepository
    OrderService ..> ProductRepository
    OrderService ..> OrderRepository
    OrderService ..> OrderItem
    App ..> OrderService
```

## Использованные технологии

- Java 17
- Spring Context
- Spring Data JPA
- Hibernate
- H2 Database
- HikariCP
- Gradle

## Вывод

В ходе лабораторной работы было разработано приложение для магазина зоотоваров с использованием JPA, Hibernate и Spring Data: настроена база данных H2 через HikariCP, созданы сущности, репозитории и сервис заказов, а также реализована загрузка данных из CSV и создание заказа в транзакции.
