# Отчет о лаботаротоной работе №4

## Цель работы

Технологии работы с базами данных. JPA. Spring Data

## Выполнение работы

Было расширено приложение новыми сущностями и приведена структура приложения в соответствие со "слоистой архитектурой".

``` mermaid
classDiagram
    %% Interfaces
    class Reader {
        <<interface>>
        +read(): String
    }

    class Parser~T~ {
        <<interface>>
        +parse(String): List~T~
    }

    class Provider~T~ {
        <<interface>>
        +getEntities(): List~T~
    }

    class Renderer {
        <<interface>>
        +render(): void
    }

    %% Domain Model
    class Product {
        +int productId
        +String name
        +String description
        +int categoryId
        +BigDecimal price
        +int stockQuantity
        +String imageUrl
        +Calendar createdAt
        +Calendar updatedAt
    }

    %% Implementations
    class ResourceFileReader {
        -String path
        +read(): String
    }

    class CSVParser~Product~ {
        +parse(String): List~Product~
        -parseLine(String[]): Product
    }

    class ConcreteProductProvider {
        -Reader reader
        -Parser~Product~ parser
        +getEntities(): List~Product~
    }

    class DataBaseRenderer {
        -JdbcTemplate jdbcTemplate
        -Provider~Product~ provider
        +render(): void
        +insertProduct(Product): void
    }

    class PropertyProvider {
        -String filename
        +getFileName(): String
    }

    class Config {
        +productParser(): Parser~Product~
        +productReader(): Reader
        +productProvider(): Provider~Product~
        +renderer(): Renderer
    }

    %% AOP
    class TimeAround {
        +invoke(MethodInvocation): Object
    }

    class TimeAroundPointCut {
        +matches(Method, Class~?~): boolean
    }

    %% Relationships
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    Provider <|.. ConcreteProductProvider
    Renderer <|.. DataBaseRenderer

    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser
    ConcreteProductProvider --> Product

    DataBaseRenderer --> JdbcTemplate
    DataBaseRenderer --> ConcreteProductProvider
    DataBaseRenderer --> Product

    ResourceFileReader --> PropertyProvider
    CSVParser --> Product

    Config ..> CSVParser : provides
    Config ..> ResourceFileReader : provides
    Config ..> ConcreteProductProvider : provides
    Config ..> DataBaseRenderer : provides
    Config ..> TimeAround
    Config ..> TimeAroundPointCut
    ``` 

## Выводы

Были изучены технологии работы с базами данных. JPA. Spring Data
