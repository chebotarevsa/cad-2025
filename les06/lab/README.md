# Отчет о лабораторной работе №3

## Цель работы

В данной работе необходимо добавить поддержку базы данных H2, реализовать сохранение категорий и продуктов через JDBC, а также выполнить SQL-запрос для получения статистики по категориям.

## Выполнение работы

В ходе работы были выполнены следующие действия:

1. Добавлен новый класс `CategoryFileReader` — реализация интерфейса `Reader` для чтения категорий из файла. Выводит дату и время инициализации через `@PostConstruct`.

![img1.png](img1.png)

2. Добавлен новый класс `ConcreteCategoryProvider` — считывает строки из файла категорий и преобразует их в список объектов `Category`.

![img2.png](img2.png)

3. Добавлен новый класс `DataBaseRenderer` — реализация интерфейса `Renderer` с аннотацией `@Primary`. Сохраняет категории и продукты в встроенную базу данных H2 через JDBC batch-insert.

![img3.png](img3.png)

4. Добавлен новый класс `CategoryRequest` — выполняет SQL-запрос к H2, выбирая категории с количеством товаров больше одного, и выводит результат через `logger.info`.

![img4.png](img4.png)

5. Приложение запускается командой `gradle run`, сохраняет данные в H2 и выводит статистику по категориям в консоль.

![img5.png](img5.png)

6. Оформлен отчет о выполнении лабораторной работы в виде файла `README.md` в директории `les06/lab`. Отчет содержит обновленную UML-диаграмму классов в формате `mermaid`.

```mermaid
classDiagram

%% ========================
%% Основные классы
%% ========================

    class App {
        +main(String[] args)
    }

    class AppConfig {
        <<Configuration>>
        <<ComponentScan>>
        <<EnableAspectJAutoProxy>>
        <<PropertySource>>
        +dataSource() DataSource
    }

%% ========================
%% Сущности
%% ========================

    class Product {
        -long id
        -String name
        -String description
        -double price
        -int stockQuantity
        -long categoryId
        +getId() long
        +getName() String
        +getDescription() String
        +getPrice() double
        +getStockQuantity() int
        +getCategoryId() long
    }

    class Category {
        -long id
        -String name
        -String description
        +getId() long
        +getName() String
        +getDescription() String
    }

%% ========================
%% Интерфейсы
%% ========================

    class Reader {
        <<interface>>
        +read() List~String~
    }

    class Parser {
        <<interface>>
        +parse(List~String~) List~Product~
    }

    class Renderer {
        <<interface>>
        +render(List~Product~) void
    }

    class ProductProvider {
        <<interface>>
        +getProducts() List~Product~
    }

%% ========================
%% Реализации Reader
%% ========================

    class ResourceFileReader {
        -String filename
        +init() void
        +read() List~String~
    }

    class CategoryFileReader {
        -String filename
        +init() void
        +read() List~String~
    }

    Reader <|.. ResourceFileReader
    Reader <|.. CategoryFileReader

%% ========================
%% Реализация Parser
%% ========================

    class CSVParser {
        +parse(List~String~) List~Product~
    }

    Parser <|.. CSVParser

%% ========================
%% Провайдеры
%% ========================

    class ConcreteProductProvider {
        -Reader reader
        -Parser parser
        +getProducts() List~Product~
    }

    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser

    class ConcreteCategoryProvider {
        -Reader reader
        +getCategories() List~Category~
    }

    ConcreteCategoryProvider --> Reader
    ConcreteCategoryProvider --> Category

%% ========================
%% Renderer-ы
%% ========================

    class ConsoleTableRenderer {
        +render(List~Product~) void
    }

    class HTMLTableRenderer {
        +render(List~Product~) void
    }

    class DataBaseRenderer {
        <<Primary>>
        -DataSource dataSource
        -ConcreteCategoryProvider categoryProvider
        +render(List~Product~) void
    }

    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    Renderer <|.. DataBaseRenderer

    DataBaseRenderer --> ConcreteCategoryProvider
    DataBaseRenderer --> Product
    DataBaseRenderer --> Category

%% ========================
%% Прочее
%% ========================

    class CategoryRequest {
        -DataSource dataSource
        +execute() void
    }

    class ParsingTimeAspect {
        <<Aspect>>
        +measureTime(ProceedingJoinPoint) Object
    }

    ParsingTimeAspect ..> CSVParser
    App --> ProductProvider
    App --> Renderer
    App --> CategoryRequest
    App --> AppConfig
```

## Выводы

В ходе выполнения работы была добавлена поддержка встроенной базы данных H2, реализовано сохранение категорий и продуктов через JDBC с использованием batch-insert, а также выполнен SQL-запрос для получения статистики по категориям с количеством товаров больше одного.
