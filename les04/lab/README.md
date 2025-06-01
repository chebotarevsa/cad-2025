# Отчет о лаботаротоной работе

## Цель работы

## Выполнение работы
- Конфигурация через `@Component`, `@Autowired`, `@Value`, `@Primary`
- Имя CSV-файла читается из `application.properties`
- Таблица сохраняется в HTML-файл (`products.html`)
- Используется AOP для замера времени парсинга
- Выводится дата инициализации бина `ResourceFileReader`

Новая диаграмма

classDiagram
    note "Товары для зоомагазина"
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider o-- Parser
    ConcreteProductProvider o-- Reader
    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    HTMLTableRenderer o-- ProductProvider
    ConsoleTableRenderer o-- ProductProvider
    ProductProvider .. Product
    Parser .. Product

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

    class Reader {
        + String read()
    }
    <<interface>> Reader

    class ResourceFileReader {
        + String read()
    }

    class Parser {
        + List[Product] parse(String)
    }
    <<interface>> Parser

    class CSVParser {
        + List[Product] parse(String)
    }

    class Renderer {
        + void render()
    }
    <<interface>> Renderer

    class HTMLTableRenderer {
        + void render()
    }

    class ConsoleTableRenderer {
        + void render()
    }

    class ProductProvider {
        + List[Product] getProducts()
    }
    <<interface>> ProductProvider

    class ConcreteProductProvider {
        + List[Product] getProducts()
    }

## Выводы