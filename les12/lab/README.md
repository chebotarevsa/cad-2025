# Отчет о лаботаротоной работе

## Цель работы

Освоить принципы построения веб-приложений с использованием Spring MVC, REST API и шаблонизатора Thymeleaf. Научиться реализовывать веб-интерфейс и REST-сервисы, а также выполнять деплой готового приложения на сервер Apache Tomcat 11.

## Выполненные шаги

1. **Перенос проекта:**
   - Результат выполнения лабораторной работы №5 был скопирован в директорию `/les12/lab`.

2. **Настройка Spring MVC:**
   - Добавлены зависимости `spring-webmvc`, `thymeleaf`, `spring-context`, `spring-data-jpa`.
   - Создан конфигурационный класс `WebConfig`, настроены `DispatcherServlet`, `ViewResolver` и ресурсные пути.
   - Обновлён `web.xml` для интеграции с Spring MVC.

3. **Реализация REST API:**
   - Создан REST-контроллер `OrderRestController` с маршрутами:
     - `GET /api/orders` — получить список заказов;
     - `GET /api/orders/{id}` — получить заказ по ID;
     - `POST /api/orders` — создать заказ;
     - `DELETE /api/orders/{id}` — удалить заказ;
     - `PUT /api/orders/{id}` — обновить заказ.
   - Для сериализации `LocalDateTime` добавлен модуль `jackson-datatype-jsr310`.

4. **Тестирование через Postman:**
   - Приложение задеплоено на Apache Tomcat 11.
   - Создана коллекция запросов в Postman для тестирования всех операций REST API.
   - Убедились, что JSON-ответы возвращаются корректно.

5. **Интерфейс на Thymeleaf:**
   - Создан контроллер `OrderController` для обработки веб-запросов.
   - Реализованы страницы:
     - `orders.html` — список заказов с кнопками "Удалить" и "Редактировать";
     - `order-form.html` — форма создания нового заказа;
     - `order-edit.html` — форма редактирования заказа.
   - Все шаблоны размещены в `/WEB-INF/templates`.

6. **Сборка WAR-файла:**
   - Команда `./gradlew clean build war` успешно выполняется.
   - WAR-файл `lab12.war` собирается в директории `build/libs`.

7. **Деплой на Tomcat:**
   - Приложение успешно развёрнуто через копирование WAR-файла в папку `webapps/`.
   - После рестарта Tomcat интерфейс и REST работают стабильно.

8. **UML-диаграмма классов (формат Mermaid):**

```mermaid
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
    }

    CustomerOrder --> Customer
    Product --> Category

##  Вывод

В ходе лабораторной работы был разработан полноценный веб-интерфейс и REST API для работы с заказами в магазине зоотоваров. Освоены ключевые компоненты Spring MVC, работа с шаблонами Thymeleaf, взаимодействие с БД через Spring Data JPA. Приложение успешно задеплоено на сервер Tomcat и протестировано с помощью Postman.



