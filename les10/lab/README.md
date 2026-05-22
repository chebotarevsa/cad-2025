# Лабораторная работа 5

Тема: разработка и развертывание Web-приложений.

В этой лабораторной работе к приложению магазина зоотоваров был добавлен простой Web-интерфейс на Java Servlet.

## Основные URL приложения

После деплоя WAR-файла в Tomcat приложение доступно по адресам:

- `http://localhost:8080/app/orders` - список заказов;
- `http://localhost:8080/app/orders/new` - форма создания заказа;
- `http://localhost:8080/app/api/products` - REST-сервис с продуктами.

## Инструкция по запуску

1. Собрать WAR-файл командой `./gradlew war`.
2. Развернуть `app.war` в Tomcat 11.
3. Открыть `http://localhost:8080/app/orders`.
4. REST-сервис проверить по адресу `http://localhost:8080/app/api/products`.

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
    class ProductRepository {
        +findAllWithCategory() List~Product~
    }
    class CustomerRepository
    class OrderRepository {
        +findAllWithDetails() List~Order~
    }
    class OrderDetailRepository

    class OrderItem {
        -Integer productId
        -Integer quantity
    }

    class OrderService {
        +createOrder(Integer customerId, List~OrderItem~ items, String shippingAddress) Order
        +findAllOrders() List~Order~
    }

    class DataLoader {
        +loadData()
    }

    class ApplicationListener {
        +contextInitialized(ServletContextEvent event)
        +contextDestroyed(ServletContextEvent event)
    }

    class OrdersServlet {
        +doGet(HttpServletRequest request, HttpServletResponse response)
    }

    class CreateOrderServlet {
        +doGet(HttpServletRequest request, HttpServletResponse response)
        +doPost(HttpServletRequest request, HttpServletResponse response)
    }

    class ProductsApiServlet {
        +doGet(HttpServletRequest request, HttpServletResponse response)
    }

    class WebUtils {
        +getBean(ServletContext servletContext, Class~T~ type) T
        +html(String value) String
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

    DataLoader ..> CategoryRepository
    DataLoader ..> CustomerRepository
    DataLoader ..> ProductRepository

    ApplicationListener ..> DataLoader
    OrdersServlet ..> OrderService
    CreateOrderServlet ..> OrderService
    CreateOrderServlet ..> CustomerRepository
    CreateOrderServlet ..> ProductRepository
    ProductsApiServlet ..> ProductRepository
    OrdersServlet ..> WebUtils
    CreateOrderServlet ..> WebUtils
    ProductsApiServlet ..> WebUtils
```

## Вывод

В ходе лабораторной работы магазин зоотоваров был преобразован в Web-приложение. Добавлены сервлеты для просмотра заказов, создания заказов и получения списка продуктов в формате JSON. Проект собирается в WAR-файл и может быть развернут на Apache Tomcat 11.
