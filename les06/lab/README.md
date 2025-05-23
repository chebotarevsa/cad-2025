# Отчет о лаботаротоной работе

## Цель работы
Технологии работы с базами данных.JDBC.
## Выполнение работы
Создан каркас приложения и разобран с конфигурированием Spring приложений на основе Java-классов. Также написали загрузчик CSV файлов который нам понадобиться в дальнейшем.
## Выводы
Были изучены технологии работы с базами данных.JDBC.

``` mermaid
classDiagram
    class Category {
        +int category_id
        +String name
        +String description
        +toString() String
    }
    
    class Product {
        +int product_id
        +String name
        +String description
        +int category_id
        +BigDecimal price
        +int stock_quantity
        +String image_url
        +Date created_at
        +Date updated_at
    }
    
    class DataProvider~T~ {
        <<interface>>
        +getItems() List~T~
    }
    
    class ConcreteCategoryProvider {
        -Reader reader
        -Parser~Category~ parser
        +getItems() List~Category~
    }
    
    class ConcreteProductProvider {
        -Reader reader
        -Parser~Product~ parser
        +getItems() List~Product~
    }
    
    class Renderer {
        <<interface>>
        +render() void
    }
    
    class DataBaseRenderer {
        -JdbcTemplate jdbcTemplate
        -DataProvider~Product~ productProvider
        -DataProvider~Category~ categoryProvider
        -Request~Category~ categoryRequest
        +render() void
        -insertCategory(Category) void
        -insertProduct(Product) void
    }
    
    class ConsoleTableRenderer {
        -DataProvider~Product~ provider
        +render() void
        +getDate(Date) String
    }
    
    class HTMLTableRenderer {
        -DataProvider~Product~ provider
        +render() void
    }
    
    class CategoryRequest {
        -JdbcTemplate jdbcTemplate
        +execute() List~Category~
    }
    
    class CSVParser~T~ {
        <<abstract>>
        +parse(String) List~T~
        +parseRow(String[]) T
        +convertToDate(String) Date
    }
    
    class CategoryParserCSV {
        +parseRow(String[]) Category
    }
    
    class ProductParserCSV {
        +parseRow(String[]) Product
    }
    
    class ResourceFileReader {
        -Provider provider
        +read(String) String
    }
    
    class ValueProvider {
        -String product_filename
        -String category_filename
        +getFileName() Map~String,String~
    }
    
    class SpringConfig {
        +dataSource() DataSource
        +jdbcTemplate(DataSource) JdbcTemplate
    }
    
    DataProvider~T~ <|-- ConcreteCategoryProvider
    DataProvider~T~ <|-- ConcreteProductProvider
    Renderer <|-- DataBaseRenderer
    Renderer <|-- ConsoleTableRenderer
    Renderer <|-- HTMLTableRenderer
    CSVParser~T~ <|-- CategoryParserCSV
    CSVParser~T~ <|-- ProductParserCSV
    DataBaseRenderer o-- JdbcTemplate
    DataBaseRenderer o-- DataProvider~Product~
    DataBaseRenderer o-- DataProvider~Category~
    DataBaseRenderer o-- CategoryRequest
    ConcreteCategoryProvider o-- Reader
    ConcreteCategoryProvider o-- Parser~Category~
    ConcreteProductProvider o-- Reader
    ConcreteProductProvider o-- Parser~Product~
    ResourceFileReader o-- Provider
    ValueProvider ..|> Provider
    CategoryParserCSV ..> Category
    ProductParserCSV ..> Product
    CategoryRequest ..> Category
```