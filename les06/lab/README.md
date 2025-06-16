# Отчет о лаботаротоной работе

## Цель работы
Технологии работы с базами данных.JDBC.
## Выполнение работы
Создан каркас приложения и разобран с конфигурированием Spring приложений на основе Java-классов. Также написали загрузчик CSV файлов который нам понадобиться в дальнейшем.Были сохранены данные в базе данных. А также выполнены SQL запросы и выведены их результаты в логи.
## Выводы
Были изучены технологии работы с базами данных.JDBC.


``` mermaid 
classDiagram
    direction TB

    class Category {
        +int productId
        +String name
        +String description
        +toString() String
    }

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

    class CategoryCSVParser {
        +parseLine(String[]) Category
    }

    class ProductCSVParser {
        +parseLine(String[]) Product
    }

    class CategoryFileReader {
        +getFilePath(PropertyProvider) String
    }

    class ProductFileReader {
        +getFilePath(PropertyProvider) String
    }

    class PropertyProvider {
        +getProductFileName() String
        +getCategoryFileName() String
    }

    class ConcreteCategoryProvider {
        -Reader reader
        -Parser~Category~ parser
        +getEntitees() List~Category~
    }

    class ConcreteProductProvider {
        -Reader reader
        -Parser~Product~ parser
        +getEntitees() List~Product~
    }

    class DataBaseRenderer {
        -JdbcTemplate jdbcTemplate
        +render()
        +insertCategory(Category)
        +insertProduct(Product)
    }

    %% Интерфейсы
    class Parser~T~ {
        <<interface>>
        +parse(String) List~T~
    }

    class Reader {
        <<interface>>
        +read() String
    }

    class Provider~T~ {
        <<interface>>
        +getEntitees() List~T~
        +getReader() Reader
        +getParser() Parser~T~
    }

    class Renderer {
        <<interface>>
        +render()
    }

    %% Связи
    CSVParser <|-- CategoryCSVParser
    CSVParser <|-- ProductCSVParser
    FileReader <|-- CategoryFileReader
    FileReader <|-- ProductFileReader

    CSVParser ..|> Parser
    FileReader ..|> Reader
    ConcreteCategoryProvider ..|> Provider
    ConcreteProductProvider ..|> Provider
    DataBaseRenderer ..|> Renderer

    CategoryCSVParser --> Category
    ProductCSVParser --> Product

    ConcreteCategoryProvider --> CategoryFileReader
    ConcreteCategoryProvider --> CategoryCSVParser
    ConcreteProductProvider --> ProductFileReader
    ConcreteProductProvider --> ProductCSVParser

    CategoryFileReader --> PropertyProvider
    ProductFileReader --> PropertyProvider

    DataBaseRenderer --> ConcreteCategoryProvider
    DataBaseRenderer --> ConcreteProductProvider
    ```
