# Отчет о лаботаротоной работе

## Цель работы
Перейти на новое, более простое конфигурирование приложения с помощью аннотаций, добавить функционал по представлению таблиц в виде HTML и измерить скорость выполнения нашего кода c помощью инструментов АОП.
## Выполнение работы
Создали класс HTMLTableRenderer для предоставления таблиц в виде HTML. С помощью событий жизненного цикла бина, вывели в консоль дату и время, когда бин ResourceFileReader был полностью инициализирован. Создали класс TimeAround и TimeAroundPointCut для измерения времени парсинга CSVParser.
## Выводы
Перешли на более простое конфигурирование приложения с помощью аннотаций. Добавили функционал по представлению таблиц в виде HTML. Измерили скорость выполнения нашего кода с помощью инструментов АОП.


``` mermaid 
classDiagram
    %% Interfaces
    class Renderer {
        <<interface>>
        +render()
    }

    class ProductProvider {
        <<interface>>
        +getProducts() List~Product~
    }

    class Parser {
        <<interface>>
        +parse(String text) List~Product~
    }

    class Reader {
        <<interface>>
        +read() String
    }

    %% Implementation Classes
    class HTMLTableRenderer {
        -provider: ProductProvider
        -DATE_FORMAT: SimpleDateFormat
        +render()
        -buildHtmlContent(List~Product~): String
        -buildTable(List~Product~): String
        -formatPrice(BigDecimal): String
        -escapeHtml(String): String
    }

    class ConcreteProductProvider {
        -reader: Reader
        -parser: Parser
        +getProducts() List~Product~
    }

    class CSVParser {
        +parse(String text) List~Product~
        -stringToCalendar(String): Calendar
        -stringToDecimal(String): BigDecimal
        -stringToInteger(String): Integer
    }

    class ResourceFileReader {
        -path: String
        +read(): String
        +init()
    }

    class Product {
        +productId: int
        +name: String
        +description: String
        +categoryId: int
        +price: BigDecimal
        +stockQuantity: int
        +imageUrl: String
        +createdAt: Calendar
        +updatedAt: Calendar
    }

    class Config {
        +parser(): Parser
    }

    class TimeAround {
        +invoke(MethodInvocation): Object
    }

    class TimeAroundPointCut {
        +matches(Method, Class~?~): boolean
    }

    class PropertyProvider {
        -filename: String
        +getFileName(): String
    }


    %% Relationships
    Renderer <|.. HTMLTableRenderer : implements
    ProductProvider <|.. ConcreteProductProvider : implements
    Parser <|.. CSVParser : implements
    Reader <|.. ResourceFileReader : implements

    HTMLTableRenderer --> ProductProvider : depends on
    ConcreteProductProvider --> Reader : depends on
    ConcreteProductProvider --> Parser : depends on
    ResourceFileReader --> PropertyProvider : depends on

    Config ..> CSVParser : creates
    Config ..> TimeAround : uses
    Config ..> TimeAroundPointCut : uses

    CSVParser --> Product : creates
    ConcreteProductProvider --> Product : returns
    ```
