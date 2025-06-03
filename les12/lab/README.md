# Отчет о лаботаротоной работе

## Цель работы
Создать веб-интерфейс для приложения магазина зоотоваров
Реализовать функционал просмотра и создания заказов через сервлеты
Настроить развертывание приложения на сервере Apache Tomcat
Обеспечить взаимодействие с REST API

## Выполнение работы

1. Подготовка окружения

Установлен Apache Tomcat 11
Добавлен пользователь с правами администратора в tomcat-users.xml:

xml
<user username="admin" password="admin" roles="manager-gui,admin-gui"/>
Настроена сборка WAR-файла в build.gradle:

gradle
plugins {
    id 'war'
}
war {
    archiveFileName = 'zoo-shop.war'
}

2. Реализация сервлетов

2.1 Список заказов (OrderListServlet)
Отображает таблицу с:

ID заказа
Данными клиента
Общей суммой
Статусом
Кнопка "Создать заказ" для перехода к форме

2.2 Форма заказа (OrderFormServlet)

java
@WebServlet("/create-order")
public class OrderFormServlet extends HttpServlet {
    // Реализация формы выбора товаров
}
2.3 REST API (ProductApiServlet)
Возвращает JSON:

json
[
  {
    "name": "Корм для собак",
    "category": "Корма",
    "stock": 50
  }
]

3. Тестирование

Postman-запрос к REST API:

http
GET http://localhost:8080/zoo-shop/api/products
Валидация данных формы заказа

Проверка корректности отображения списка заказов

4. Развертывание
Сборка:

bash
gradle war
Деплой zoo-shop.war через Tomcat Manager

## Выводы сделал
Реализовано полноценное веб-приложение с:
Интерфейсом на сервлетах
REST API для интеграции
Взаимосвязанными сущностями заказов и товаров
Настроен процесс CI/CD:
Автоматическая сборка WAR-файла
Деплой на Tomcat
Достигнуты цели работы:
Рабочий веб-интерфейс
Корректное взаимодействие с БД
Готовность к масштабированию

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
        +addOrderDetail(OrderDetail)
        +calculateTotalPrice() BigDecimal
    }

    class OrderDetail {
        +Integer id
        +Integer quantity
        +BigDecimal price
    }

    class OrderListServlet {
        +doGet(HttpServletRequest, HttpServletResponse)
    }

    class OrderFormServlet {
        +doGet(HttpServletRequest, HttpServletResponse)
        +doPost(HttpServletRequest, HttpServletResponse)
    }

    class ProductApiServlet {
        +doGet(HttpServletRequest, HttpServletResponse)
    }

    Customer "1" -- "*" CustomerOrder : places
    CustomerOrder "1" -- "*" OrderDetail : contains
    OrderDetail "*" -- "1" Product : refers to
    Product "1" -- "1" Category : belongs to
    
    OrderListServlet --> CustomerOrder : uses
    OrderFormServlet --> Product : uses
    OrderFormServlet --> Customer : uses
    ProductApiServlet --> Product : uses
```