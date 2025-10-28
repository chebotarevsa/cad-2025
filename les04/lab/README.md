# Отчет о лаботаротоной работе

## Цель работы
В этой работе нам необходимо перейти на новое, более простое конфигурирование приложения с помощью аннотаций, добавить функционал по представлению таблиц в виде HTML и измерить скорость выполнения нашего кода c помощью инструментов АОП.

## Выполнение работы

Скопируйте результат выполнения лабораторной работы № 1 в директорию /les04/lab/

Переделайте приложение так, чтобы его конфигурирование осуществлялось с помощью аннотаций @Component

Используя аннотацию @Value и SpEL сделайте так, чтобы имя файла для загрузки продуктов, приложение получало из конфигурационного файла application.properties. Данный файл поместите в каталог ресурсов (src/main/resources)

Добавьте еще одну имплементацию интерфейса Renderer - HTMLTableRenderer которая выводит таблицу в HTML-файл. Сделайте так, чтобы при работе приложения вызывалась эта реализация, а не ConsoleTableRenderer.

С помощью событий жизненного цикла бина, выведите в консоль дату и время, когда бин ResourceFileReader был полностью инициализирован.

С помощью инструментов AOП замерьте сколько времени тратиться на парсинг CSV файла.

Приложение должно запускаться с помощью команды gradle run, выводить необходимую информацию в консоль и успешно завершаться.

Оформите отчет о выполнении лабораторной работы в виде файла README.md в директории les04/lab. Отчет должен содержать обновленную UML-диаграмму классов в формате mermaid.

``` mermaid 
classDiagram
    %% Интерфейсы
    class Reader {
        <<interface>>
        +read(): String
    }

    class Parser {
        <<interface>>
        +parse(String): List~Product~
    }

    class ProductProvider {
        <<interface>>
        +getProducts(): List~Product~
    }

    class Renderer {
        <<interface>>
        +render(): void
    }

    %% Сущность
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

    %% Реализации
    class ResourceFileReader {
        +read(): String
    }

    class CSVParser {
        +parse(String): List~Product~
    }

    class ConcreteProductProvider {
        -Reader reader
        -Parser parser
        +getProducts(): List~Product~
    }

    class ConsoleTableRenderer {
        -ProductProvider provider
        +render(): void
    }

    %% Отношения
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    ProductProvider <|.. ConcreteProductProvider
    Renderer <|.. ConsoleTableRenderer

    ConcreteProductProvider --> ResourceFileReader : uses
    ConcreteProductProvider --> CSVParser : uses
    ConcreteProductProvider --> Product : returns

    ConsoleTableRenderer --> ConcreteProductProvider : depends on
    ConsoleTableRenderer --> Product : displays

    CSVParser --> Product : parses

    ```

## Выводы
