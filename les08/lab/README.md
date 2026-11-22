# Лабораторная работа №4
# Технологии работы с базами данных. JPA. Spring Data

## Ход работы
В данной лабораторной работе был выполнен переход с использования Spring JDBC на использование ORM Hibernate. 
Приложение было расширено новыми сущностями, а его структура была приведена в соответствие со слоистой архитектурой.
В приложении был создан DataSource, соответствующий требованиям лабораторной работы. 
В качестве базы данных была использована H2. 
Для реализации DataSource была подключена библиотека HikariCP, а именно класс HikariDataSource.
Для работы с базой данных была подключена библиотека Hibernate, использующая технологию ORM. 
Схема базы данных создавалась автоматически на основании JPA-сущностей.

1) В пакете ru.bsuedu.cad.lab.entity были созданы JPA-сущности, описывающие структуру базы данных. 
Для сущностей были использованы аннотации JPA, такие как @Entity, @Table, @Id, @ManyToOne, @OneToMany и @JoinColumn.

2) В пакете ru.bsuedu.cad.lab.repository были реализованы репозитории для каждой сущности. 
Репозитории содержали методы для создания записи, получения записи по идентификатору и получения всех записей.

3) В пакете ru.bsuedu.cad.lab.service были созданы сервисы для создания заказа и получения списка всех заказов. 
Создание заказа выполнялось в рамках транзакции с использованием аннотации @Transactional.

5) В пакете ru.bsuedu.cad.lab.app был реализован клиент для сервиса создания заказа. 
6) При запуске приложения выполнялась загрузка данных из CSV-файлов category.csv, customer.csv и product.csv, после чего создавался новый заказ.

Информация о создании заказа была выведена в лог. 
Для подтверждения сохранения заказа в базе данных было выполнено получение списка всех заказов. 
В результате в консоль была выведена информация о созданном заказе и количестве заказов в базе данных.

```mermaid
classDiagram
    class App {
        +main(String[] args)
    }

    class AppConfig {
        +dataSource() DataSource
        +entityManagerFactory(DataSource) LocalContainerEntityManagerFactoryBean
        +transactionManager(EntityManagerFactory) PlatformTransactionManager
    }

    class DataLoaderService {
        -CategoryRepository categoryRepository
        -ProductRepository productRepository
        -CustomerRepository customerRepository
        +loadData()
    }

    class OrderService {
        -CustomerRepository customerRepository
        -ProductRepository productRepository
        -OrderRepository orderRepository
        +createOrder(Long customerId, Long productId, int quantity) OrderEntity
        +findAllOrders() List~OrderEntity~
    }

    class CategoryRepository {
        -EntityManager entityManager
        +save(Category category)
        +findById(Long id) Category
        +findAll() List~Category~
    }

    class ProductRepository {
        -EntityManager entityManager
        +save(Product product)
        +findById(Long id) Product
        +findAll() List~Product~
    }

    class CustomerRepository {
        -EntityManager entityManager
        +save(Customer customer)
        +findById(Long id) Customer
        +findAll() List~Customer~
    }

    class OrderRepository {
        -EntityManager entityManager
        +save(OrderEntity order)
        +findById(Long id) OrderEntity
        +findAll() List~OrderEntity~
    }

    class OrderItemRepository {
        -EntityManager entityManager
        +save(OrderItem orderItem)
        +findById(Long id) OrderItem
        +findAll() List~OrderItem~
    }

    class Category {
        -Long id
        -String name
        -String description
    }

    class Product {
        -Long id
        -String name
        -String description
        -BigDecimal price
        -Integer stockQuantity
        -String imageUrl
        -LocalDate createdAt
        -LocalDate updatedAt
        -Category category
    }

    class Customer {
        -Long id
        -String name
        -String email
        -String phone
        -String address
    }

    class OrderEntity {
        -Long id
        -LocalDateTime createdAt
        -Customer customer
        -List~OrderItem~ items
    }

    class OrderItem {
        -Long id
        -Integer quantity
        -OrderEntity order
        -Product product
    }

    App --> AppConfig
    App --> DataLoaderService
    App --> OrderService

    DataLoaderService --> CategoryRepository
    DataLoaderService --> ProductRepository
    DataLoaderService --> CustomerRepository

    OrderService --> CustomerRepository
    OrderService --> ProductRepository
    OrderService --> OrderRepository

    CategoryRepository --> Category
    ProductRepository --> Product
    CustomerRepository --> Customer
    OrderRepository --> OrderEntity
    OrderItemRepository --> OrderItem

    Product --> Category
    OrderEntity --> Customer
    OrderEntity --> OrderItem
    OrderItem --> Product
    OrderItem --> OrderEntity
```

## Вывод
Были изучены технологии работы с базами данных, JPA и Spring Data.

