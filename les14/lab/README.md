# Отчет о лаботаротоной работе
## Цель работы
Интегрировать Spring Security в существующее веб-приложение магазина зоотоваров
Реализовать ролевой доступ (USER и MANAGER)
Настроить два типа аутентификации:
    Форму входа для веб-интерфейса
    Basic Authentication для REST API
Обеспечить безопасность всех эндпоинтов приложения

## Выполнение работы
1. Настройка окружения
Добавлены зависимости в build.gradle:
gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'

2. Конфигурация безопасности
Создан класс SecurityConfig

3. Обновление веб-интерфейса
Добавлены элементы безопасности в Thymeleaf-шаблоны:

html
<div sec:authorize="isAuthenticated()">
    Добро пожаловать, <span sec:authentication="name"></span>!
    <form th:action="@{/logout}" method="post">
        <button type="submit">Выйти</button>
    </form>
</div>

4. Тестирование
Проверены сценарии:
Доступ USER: только просмотр заказов
Доступ MANAGER: полное управление заказами
Basic Auth для API:
bash
curl -u manager:manager123 http://localhost:8080/api/orders

5. Сборка и деплой
Собрано WAR-приложение:

bash
gradle war
Развернуто на Apache Tomcat 11 через Manager UI

## Выводы сделал
Успешно интегрирован Spring Security в приложение
Реализовано разграничение прав:
USER: просмотр заказов
MANAGER: полное управление заказами и API
Настроены два типа аутентификации:
Форма входа для пользователей
Basic Auth для автоматизированных систем

``` mermaid
classDiagram
    class Customer {
        +Integer id
        +String name
        +String email
        +String phone
        +String address
    }

    class Product {
        +Integer id
        +String name
        +String description
        +BigDecimal price
        +int stockQuantity
        +String imageUrl
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }

    class Category {
        +Integer id
        +String name
        +String description
    }

    class CustomerOrder {
        +Integer id
        +LocalDateTime orderDate
        +BigDecimal totalPrice
        +String status
        +String shippingAddress
    }

    class OrderDetail {
        +Integer id
        +int quantity
        +BigDecimal price
    }

    class SecurityConfig {
        +SecurityFilterChain securityFilterChain()
        +UserDetailsService userDetailsService()
        +PasswordEncoder passwordEncoder()
    }

    Customer "1" -- "*" CustomerOrder : places
    CustomerOrder "1" -- "*" OrderDetail : contains
    Product "1" -- "*" OrderDetail : included in
    Category "1" -- "*" Product : categorizes
    
    class OrderRestController {
        +OrderRepository orderRepository
        +getAllOrders()
        +getOrderById()
        +createOrder()
        +updateOrder()
        +partialUpdateOrder()
        +deleteOrder()
    }

    class OrderController {
        +CustomerOrderRepository orderRepo
        +CustomerRepository customerRepo
        +ProductRepository productRepo
        +listOrders()
        +showCreateForm()
        +createOrder()
        +deleteOrder()
        +showEditForm()
        +updateOrder()
    }

    OrderRestController --> CustomerOrder : manages
    OrderController --> CustomerOrder : manages
```