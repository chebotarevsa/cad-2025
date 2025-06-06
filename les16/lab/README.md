# Отчет о лаботаротоной работе

## Цель работы
1. Настроить систему автоматического тестирования для Spring-приложения
2. Реализовать модульные (Unit) и интеграционные тесты
3. Обеспечить контроль качества через отчёты JaCoCo

## Выполнение работы
Unit-тесты (модульные):

Тестирование сервиса заказов без зависимостей от БД

Пример:

java
@Test
void createOrderWithEmptyProductsShouldFail() {
    assertThrows(IllegalArgumentException.class,
        () -> orderService.createOrder(customer, Collections.emptyList()));
}

Интеграционные тесты:

Проверка взаимодействия с реальной БД H2

Решение проблемы дублирования ID:

java
@Transactional
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class OrderIntegrationTest {
    // ...
}
3. Отчётность
Настроен JaCoCo для анализа покрытия:

xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
</plugin>

## Выводы сделал
Достигнуто:
Полное покрытие критически важных методов
Изолированное тестирование компонентов
Автоматизированная проверка целостности данных

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
        +Integer stockQuantity
    }
    
    class CustomerOrder {
        +Integer id
        +Customer customer
        +LocalDateTime orderDate
        +BigDecimal totalPrice
        +String status
    }
    
    class OrderDetail {
        +Integer id
        +CustomerOrder order
        +Product product
        +Integer quantity
        +BigDecimal price
    }
    
    class OrderService {
        +createOrder(Customer, List~Product~) CustomerOrder
    }
    
    class OrderRepository {
        +save(CustomerOrder) CustomerOrder
        +findById(Integer) Optional~CustomerOrder~
    }
    
    class ProductRepository {
        +save(Product) Product
        +findById(Integer) Optional~Product~
    }
    
    Customer "1" -- "*" CustomerOrder
    CustomerOrder "1" -- "*" OrderDetail
    Product "1" -- "*" OrderDetail
    
    OrderService --> OrderRepository
    OrderService --> OrderDetailRepository
    OrderService --> CustomerRepository
    OrderService --> ProductRepository
```