# Отчет о лаботаротоной работе

## Цель работы
Перейти на новое, более простое конфигурирование приложения с помощью аннотаций, добавить функционал по представлению таблиц в виде HTML и измерить скорость выполнения нашего кода c помощью инструментов АОП.
## Выполнение работы
Создали класс HTMLTableRenderer для предоставления таблиц в виде HTML.
С помощью событий жизненного цикла бина, вывели в консоль дату и время, когда бин ResourceFileReader был полностью инициализирован.
Создали класс TimeAround и TimeAroundPointCut для измерения времени парсинга CSVParser.
## Выводы сделал
Перешли на более простое конфигурирование приложения с помощью аннотаций.
Добавили функционал по представлению таблиц в виде HTML. 
Измерили скорость выполнения нашего кода с помощью инструментов АОП.

``` mermaid
classDiagram
    class App {
        +main()
    }

    class Product {
        +Long productId
        +String name
        +String description
        +Integer categoryId
        +BigDecimal price
        +Integer stockQuantity
        +String imageUrl
        +Date createdAt
        +Date updatedAt
    }

    class Renderer {
        <<interface>>
        +render()
    }

    class ProductProvider {
        <<interface>>
        +getProducts()
    }

    class Parser {
        <<interface>>
        +parse(String text)
    }

    class Reader {
        <<interface>>
        +read()
    }

    class ConsoleTableRenderer {
        -ProductProvider provider
        +render()
    }

    class HTMLTableRenderer {
        -ProductProvider provider
        +render()
    }

    class ConcreteProductProvider {
        -Reader reader
        -Parser parser
        +getProducts()
    }

    class CSVParser {
        +parse(String text)
    }

    class ResourceFileReader {
        -String fileName
        +read()
    }

    class FilePathProvider {
        -String fileName
        +getFileName()
    }

    class PerfomanceAdvice {
        +invoke(MethodInvocation invocation)
    }

    class Config {
        +parser() Parser
    }

    App --> Config
    App --> Renderer
    Renderer <|-- ConsoleTableRenderer
    Renderer <|-- HTMLTableRenderer
    ProductProvider <|-- ConcreteProductProvider
    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser
    Parser <|-- CSVParser
    Reader <|-- ResourceFileReader
    ResourceFileReader --> FilePathProvider
    Config --> PerfomanceAdvice
    PerfomanceAdvice ..> CSVParser : advises
```