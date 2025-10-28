# Отчёт по лабораторной работе 4

## Тема
Технологии работы с базами данных. JDBC

## Цель работы
Подключить H2 БД, создать таблицы, сохранить данные, выполнить запрос с логами.

## Выполненные действия
1. Скопирован проект из les04/lab.
2. Добавлены зависимости H2, starter-jdbc, logback.
3. Создан schema.sql для таблиц CATEGORIES и PRODUCTS.
4. Настроен EmbeddedDatabaseBuilder в Main.java.
5. Создан Category, CategoryProvider, ConcreteCategoryProvider и category.csv.
6. Реализован DataBaseRenderer для сохранения в БД.
7. Реализован CategoryRequest для запроса и логирования INFO.
8. Запуск через gradle run работает.

## Результат
БД запускается, данные сохраняются, запрос выводит логи.


```mermaid
classDiagram
    class Product {
        -int productId
        -String name
        -String description
        -int categoryId
        -double price
        -int stockQuantity
        -String imageUrl
        -Timestamp createdAt
        -Timestamp updatedAt
    }
    class Category {
        -int categoryId
        -String name
        -String description
    }
    class CategoryProvider {
        <<interface>>
        +List~Category~ getCategories()
    }
    class ConcreteCategoryProvider {
        -String filePath
        +List~Category~ getCategories()
    }
    class DataBaseRenderer {
        -JdbcTemplate jdbcTemplate
        +void render(List~Product~)
    }
    class CategoryRequest {
        -JdbcTemplate jdbcTemplate
        +void executeQuery()
    }
    Product }|..|| Category : categoryId
    ConcreteCategoryProvider ..|> CategoryProvider
    DataBaseRenderer ..|> Renderer
    CategoryRequest -- JdbcTemplate
    Main -- DataBaseRenderer
    Main -- CategoryRequest