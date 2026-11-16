# Отчет о лаботаротоной работе №2

## Цель работы

В данной работе необходимо перейти на новое, более простое конфигурирование приложения с помощью аннотаций, добавить функционал по представлению таблиц в виде HTML и измерить скорость выполнения нашего кода c помощью инструментов АОП.

## Выполнение работы

В ходе работы были выполнены следующие действия:

<!-- 1. Переделайте приложение так, чтобы его конфигурирование осуществлялось с помощью аннотаций `@Component`

![img1.png)](img1.png)

2. Использую аннотацию `@Value` и SpEL сделайте так, чтобы имя файла для загрузки продуктов, приложение получало из конфигурационного файла `application.properties`. Данный файл поместите в каталог ресурсов `(src/main/resources)`

![img2.png)](img2.png) -->

1. Добавьте еще одну имплементацию интерфейса Renderer - `HTMLTableRenderer`которая выводит таблицу в HTML-файл. Сделайте так, чтобы при работе приложения вызывалась эта реализация, а не `ConsoleTableRenderer`.

![img1.png)](img1.png)

2. С помощью событий жизненного цикла бина, выведите в консоль дату и время, когда бин `ResourceFileReader` был полностью инициализирован.

![img2.png)](img2.png)

3. С помощью инструментов AOП замерьте сколько времени тратиться на парсинг CSV файла.

![img3.png)](img3.png)

4. Приложение должно запускаться с помощью команды `gradle run`, выводить необходимую информацию в консоль и успешно завершаться.

![img4.png)](img4.png)

![img5.png)](img5.png)

5. Оформите отчет о выполнении лабораторной работы в виде файла `README.md` в директории `les04/lab`. Отчет должен содержать обновленную UML-диаграмму классов в формате `mermaid`.

```mermaid
classDiagram
    note "Товары для зоомагазина"

    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider o-- Parser
    ConcreteProductProvider o-- Reader
    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    ConsoleTableRenderer ..> ProductProvider
    HTMLTableRenderer ..> ProductProvider
    ProductProvider ..> Product
    Parser ..> Product
    App ..> ProductProvider
    App ..> Renderer
    AppConfig ..> App
    ParsingTimeAspect ..> CSVParser

    class Product {
        -long id
        -String name
        -String description
        -double price
        -int stockQuantity
        +Product(long, String, String, double, int)
        +getId() long
        +getName() String
        +getDescription() String
        +getPrice() double
        +getStockQuantity() int
    }

    class Reader {
        <<interface>>
        +List~String~ read()
    }

    class ResourceFileReader {
        -String filename
        +init() void
        +read() List~String~
    }

    class Parser {
        <<interface>>
        +List~Product~ parse(List~String~)
    }

    class CSVParser {
        +parse(List~String~) List~Product~
    }

    class Renderer {
        <<interface>>
        +render(List~Product~) void
    }

    class ConsoleTableRenderer {
        +render(List~Product~) void
    }

    class HTMLTableRenderer {
        <<Primary>>
        +render(List~Product~) void
    }

    class ProductProvider {
        <<interface>>
        +List~Product~ getProducts()
    }

    class ConcreteProductProvider {
        -Reader reader
        -Parser parser
        +ConcreteProductProvider(Reader, Parser)
        +getProducts() List~Product~
    }

    class App {
        +main(String[]) void$
    }

    class AppConfig {
        <<Configuration>>
        <<ComponentScan>>
        <<EnableAspectJAutoProxy>>
        <<PropertySource>>
    }

    class ParsingTimeAspect {
        <<Aspect>>
        +measureTime(ProceedingJoinPoint) Object
    }
```

## Выводы

В ходе выполнения работы произошел переход на новое, более простое конфигурирование приложения с помощью аннотаций, добавлен функционал по представлению таблиц в виде HTML и была измерена скорость выполнения кода c помощью инструментов АОП.
