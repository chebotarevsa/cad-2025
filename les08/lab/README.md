# Отчет о лаботаротоной работе

## Цель работы
В этой работе мы перейдем с использование Spring JDBC на использование ORM Hibernate и Spring Data. Расширим наше приложение новыми сущностями и приведем структуру приложения в соответствие со "слоистой архитектурой".

## Выполнение работы
Созданий новое приложение или скопируйте результат выполнения лабораторной работы №3 в директорию /les08/lab/. Изменений будет много, возможно для вас будет проще создать проект заново.

Создайте DataSource соответствующий следующим требованиям

- Должна использоваться база данных H2
- Для реализации DataSource необходимо использовать библиотеку HikariCP, а именно HikariDataSource
- Для работы с базой данных должна использоваться библиотека HIbernate, использующая технологию ORM
- Схема схема данных должна создаваться автоматически на основании JPA сущностей.

Структура пакетов проекта должна иметь следующий вид

` ru.bsuedu.cad.lab - основной пакет
- ru.bsuedu.cad.lab.entity - JPA сущности
- ru.bsuedu.cad.lab.repository - репозитории
- ru.bsuedu.cad.lab.service - сервисы
- ru.bsuedu.cad.lab.app - приложение

В пакете ru.bsuedu.cad.lab.entity создайте JPA сущности для следующей схемы базы данных.

В пакете ru.bsuedu.cad.lab.repository реализуйте репозитории для каждой сущности. Репозитории содержать методы по созданию, получение записи по идентификатору и получения всех записей для каждой сущности.

В пакете ru.bsuedu.cad.lab.service создайте сервисы для создания заказа и получению списка всех заказов.

В пакете ru.bsuedu.cad.lab.app реализуйте клиент для сервиса создания заказа, который создает новый заказ. Создание заказа должно выполняться в рамках транзакции. Выведите информацию о создании заказа в лог. Докажите, что заказ сохранился в базе данных. (Для того, чтобы создать заказ, необходимо заполнить таблицы базы данных на основании CSV файлов (category.csv, customer.csv, product.csv). Сделайте это любым удобным для вас способом. )

Приложение должно запускаться с помощью команды gradle run, выводить необходимую информацию в консоль и успешно завершаться.

Оформите отчет о выполнении лабораторной работы в виде файла README.md в директории les08/lab. Отчет должен содержать обновленную UML-диаграмму классов в формате mermaid.


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
