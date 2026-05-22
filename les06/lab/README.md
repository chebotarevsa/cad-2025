# Лабораторная работа 4

## Тема

Технологии работы с базами данных. JDBC.

## Цель

Подключить к приложению базу данных H2, сохранить в нее данные из CSV-файлов и выполнить SQL-запрос через JDBC.

## Что сделано

- В проект добавлена база данных H2.
- В AppConfig настроены EmbeddedDatabaseBuilder и JdbcTemplate.
- Создан SQL-скрипт schema.sql с таблицами CATEGORIES и PRODUCTS.
- Добавлен класс Category и провайдер ConcreteCategoryProvider.
- Файл category.csv добавлен в src/main/resources.
- Добавлен DataBaseRenderer, который сохраняет категории и продукты в базу данных.
- Добавлен CategoryRequest, который выводит категории, где товаров больше одного.
- Для вывода результата используется logback с уровнем INFO.

## UML-диаграмма

```mermaid
classDiagram
    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    Renderer <|.. DataBaseRenderer

    ProductProvider <|.. ConcreteProductProvider
    CategoryProvider <|.. ConcreteCategoryProvider
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser

    App --> Renderer
    AppConfig --> DataSource
    AppConfig --> JdbcTemplate

    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser
    ConcreteProductProvider --> Product
    ConcreteCategoryProvider --> Category

    DataBaseRenderer --> ProductProvider
    DataBaseRenderer --> CategoryProvider
    DataBaseRenderer --> JdbcTemplate
    DataBaseRenderer --> CategoryRequest
    CategoryRequest --> JdbcTemplate

    class App {
        +main(String[] args) void
    }

    class AppConfig {
        +dataSource() DataSource
        +jdbcTemplate(DataSource) JdbcTemplate
    }

    class Renderer {
        <<interface>>
        +render() void
    }

    class DataBaseRenderer {
        +render() void
    }

    class ProductProvider {
        <<interface>>
        +getProducts() List~Product~
    }

    class CategoryProvider {
        <<interface>>
        +getCategories() List~Category~
    }

    class Reader {
        <<interface>>
        +read() String
    }

    class Parser {
        <<interface>>
        +parse(String value) List~Product~
    }

    class Product {
        +long productId
        +String name
        +String description
        +int categoryId
        +BigDecimal price
        +int stockQuantity
        +String imageUrl
        +Date createdAt
        +Date updatedAt
    }

    class Category {
        +long categoryId
        +String name
        +String description
    }

    class CategoryRequest {
        +printCategoriesWithMoreThanOneProduct() void
    }
```

## Вывод

Приложение сохраняет данные из CSV-файлов в таблицы H2 и выводит результат SQL-запроса в консоль.
