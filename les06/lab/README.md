# Отчет о лаботаротоной работе

## Цель работы
В этой работе нам необходимо научить наше приложение сохранять данные в базе данных. А также научить наше приложение выполнять SQL запросы и выводить их результаты в логи. В этом нам поможет механизм JDBC (Java Database Connectivity), и такие инструменты Spring как DataSource, JDBCTemplate, RowMapper упрощающие работу с JDBC.

## Выполнение работы
Скопируйте результат выполнения лабораторной работы № 2 в директорию /les06/lab/

Подключите к приложению встраиваемую базу данных H2 используя EmbeddedDatabaseBuilder

Напишите SQL скрипт создающие две таблицы "Продукты" (PRODUCTS) и "Категории" (CATEGORIES) (не забудьте про внешние ключи).

Настройте EmbeddedDatabaseBuilder так, чтобы он при старте приложения выполнял данный скрипт и создавал в базе данных таблицы CATEGORIES и PRODUCTS.

Для таблицы "Категории" создайте Java класс Category, для моделирования данной сущности (аналогичный классу Product). И класс ConcreteCategoryProvider, аналогичный ConcreteProductProvider, данный класс должен предоставлять данные из CSV файла category.csv. CSV-файл должен располагаться в директории src/main/resources вашего приложения

Добавьте еще одну имплементацию интерфейса Renderer - DataBaseRenderer которая сохраняет данные считанные из SCV файлов в таблицы базы данных. Реализация DataBaseRenderer должна использоваться пол умолчанию.

Реализуйте класс CategoryRequest, данный класс должен выполнять запрос к базе данных получающий следующую информацию - список категорий количество товаров в которых больше единицы. Данная информация должна выводиться в консоль с помощью библиотеки для логирования logback, уровень лога INFO.

Приложение должно запускаться с помощью команды gradle run, выводить необходимую информацию в консоль и успешно завершаться.

Оформите отчет о выполнении лабораторной работы в виде файла README.md в директории les06/lab. Отчет должен содержать обновленную UML-диаграмму классов в формате mermaid.


``` mermaid 
classDiagram
    direction TB

    %% Интерфейсы (в другом порядке)
    class Renderer {
        <<interface>>
        +render()
    }

    class Provider~T~ {
        <<interface>>
        +getEntitees() List~T~
        +getReader() Reader
        +getParser() Parser~T~
    }

    class Parser~T~ {
        <<interface>>
        +parse(String) List~T~
    }

    class Reader {
        <<interface>>
        +read() String
    }

    %% Классы данных
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

    class Category {
        +int productId
        +String name
        +String description
        +toString() String
    }

    %% CSV Парсеры и Чтение файлов
    class ProductCSVParser {
        +parseLine(String[]) Product
    }

    class CategoryCSVParser {
        +parseLine(String[]) Category
    }

    class ProductFileReader {
        +getFilePath(PropertyProvider) String
    }

    class CategoryFileReader {
        +getFilePath(PropertyProvider) String
    }

    class CSVParser~T~ {
        <<abstract>>
        +parse(String) List~T~
        #parseLine(String[]) T
        #stringToInteger(String) Integer
        #stringToDecimal(String) BigDecimal
        #stringToCalendar(String) Calendar
    }

    class FileReader {
        <<abstract>>
        -String path
        +read() String
        #getFilePath(PropertyProvider) String
    }

    %% Поставщики данных
    class ConcreteProductProvider {
        -Reader reader
        -Parser~Product~ parser
        +getEntitees() List~Product~
    }

    class ConcreteCategoryProvider {
        -Reader reader
        -Parser~Category~ parser
        +getEntitees() List~Category~
    }

    %% Дополнительные классы
    class PropertyProvider {
        +getProductFileName() String
        +getCategoryFileName() String
    }

    class DataBaseRenderer {
        -JdbcTemplate jdbcTemplate
        +render()
        +insertCategory(Category)
        +insertProduct(Product)
    }

    %% Наследование и реализация
    ProductCSVParser <|-- CSVParser
    CategoryCSVParser <|-- CSVParser
    ProductFileReader <|-- FileReader
    CategoryFileReader <|-- FileReader

    CSVParser ..|> Parser
    FileReader ..|> Reader

    ConcreteProductProvider ..|> Provider
    ConcreteCategoryProvider ..|> Provider
    DataBaseRenderer ..|> Renderer

    %% Ассоциации
    ProductCSVParser --> Product
    CategoryCSVParser --> Category

    ConcreteProductProvider --> ProductFileReader
    ConcreteProductProvider --> ProductCSVParser
    ConcreteCategoryProvider --> CategoryFileReader
    ConcreteCategoryProvider --> CategoryCSVParser

    ProductFileReader --> PropertyProvider
    CategoryFileReader --> PropertyProvider

    DataBaseRenderer --> ConcreteProductProvider
    DataBaseRenderer --> ConcreteCategoryProvider
```


## Выводы
