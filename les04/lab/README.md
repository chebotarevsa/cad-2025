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
    note "Товары для зоомагазина"
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider o-- Parser
    ConcreteProductProvider o-- Reader
    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    ConsoleTableRenderer o-- ProductProvider
    ProductProvider .. Product
    Parser .. Product
    TimeAroundPointCut --|> NameMatchMethodPointcut
    CSVParser ..> TimeAround
    ResourceFileReader o-- PropertyProvider
    Config --> TimeAround
    Config --> TimeAroundPointCut



    class  Product {
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

    class  Reader{
        + String read()
    }
    <<interface>> Reader

    class ResourceFileReader {
        + String read()
    }

    class  Parser{
        + List[Product] parse(String)
    }
    <<interface>> Parser

    class CSVParser {
        + List[Product] parse(String)
    }

    class  Renderer{
        +void render()
    }
    <<interface>> Renderer

    class ConsoleTableRenderer {
        - ProductProvider provider
        +void render()
    }


    class ProductProvider {
        + List[Product] getProducts()
    }
    <<interface>> ProductProvider

    class ConcreteProductProvider{
        - Reader reader
        - Parser parser
       + List[Product] getProducts()
    }

    class Config {
        +Parser parser()
    }

    class HTMLTableRenderer{
        - ProductProvider provider
        +void render()
    }

    class TimeAround {
        +Object invoke(MethodInvocation invocation)
    }
    <<interface>> TimeAround

    class TimeAroundPointCut {
        +boolean matches(Method method, Class<?> targetClass)
    }

    class PropertyProvider {
    +String getFileName()
    }
```