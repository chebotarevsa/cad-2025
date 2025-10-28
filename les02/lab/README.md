# Отчет о лаботаротоной работе

## Цель работы

## Выполнение работы
Скачайте и установите Axiom Standard JDK 17.0.14 (предпочтительно) или Temurin JDK 17.0.14. Используйте руководство по установке.

Скачайте и установите Gradle 8.12. Используйте руководство по установке.

С помощью Gradle, в директории les02/lab создайте проект вид Java Applications, для этого воспользуйтесь руководством Building Java Applications Sample.

Добавьте в ваш проект библиотеку org.springframework:spring-context:6.2.2

Реализуйте приложение удовлетворяющее следующим требованиям:
- Приложение должно представлять собой консольное приложение разработанное с помощью фреймворка Spring и конфигурируемое с помощью Java-конфигурации.
- Приложение должно читать данные о товарах для магазина из csv-файла и выводить его в консоль в виде таблицы (см. рисунок).

CSV-файл должен располагаться в директории src/main/resources вашего приложения.

- Структура приложения должна соответствовать следующей диаграмме классов

где,
- Reader, ResourceFileReader - класс читающий данные из csv-файла;
- Parser, CSVParser - класс парсер CSV файла;
- ProductProvider, ConcreteProductProvider - класс предоставляющий список товаров;
- Renderer, ConsoleTableRenderer - класс выводящий список товаров в консоль в виде таблицы;
- Product - класс описывающий сущность "Товар"

Приложение должно запускаться с помощью команды gradle run, выводить необходимую информацию в консоль и успешно завершаться.

Оформите отчет о выполнении лабораторной работы в виде файла README.md в директории les02/lab.

 ``` mermaid 

classDiagram
    note "Товары для зоомагазина"
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider o-- Parser
    ConcreteProductProvider o-- Reader
    Renderer <|.. ConsoleTableRenderer
    ConsoleTableRenderer o-- ProductProvider
    ProductProvider .. Product
    Parser .. Product

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
    ```


## Выводы