# Отчет о лаботаротоной работе

## Цель работы
Что за приложение без интерфейса? На этом занятии мы начнем добавление Web-приложений нашему приложению. Магазин зоотоваров 🐣 получит первый интерфейс.

## Выполнение работы
Скопируйте результат выполнения лабораторной работы №4 в директорию /les10/lab/.

Скачайте, установите и настройте Apache Tomcat 11 https://tomcat.apache.org/.

Добавьте пользователя с правами администратора.

Настройте проект, так чтобы в результате сборки формировался WAR-файл.

Реализуйте Java-сервлет формирующий Web-страницу с информацией о заказах. Страница должна содержать кнопку для перехода на форму создания заказа.

Реализуйте Java-сервлет формирующий Web-страницу с формой для создания заказа. После создания заказа должен открываться список заказов.

Реализуйте Java-сервлет представляющий REST сервис для получения информации о продуктах. Для каждого продукта необходимо вывести следующую информацию: Название продукта, название категории, количество на складе.

Соберите приложение используя команду gradle war.

Выполните деплой приложения на сервер Apache Tomcat 11. Протестируйте работу REST сервиса используя Postman https://www.postman.com/

Оформите отчет о выполнении лабораторной работы в виде файла README.md в директории les10/lab. Отчет должен содержать обновленную UML-диаграмму классов в формате mermaid.


``` mermaid
classDiagram
    %% Интерфейсы
    class Reader {
        <<interface>>
        +read(): String
    }
    class Parser~T~ {
        <<interface>>
        +parse(String): List~T~
    }
    class Provider~T~ {
        <<interface>>
        +getEntities(): List~T~
    }
    class Renderer {
        <<interface>>
        +render(): void
    }

    %% Сущности
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
    class Category {
        +int categoryId
        +String name
        +String description
    }

    %% Реализации чтения и парсинга
    class ResourceFileReader {
        -String path
        +read(): String
    }
    class CSVParser~Product~ {
        +parse(String): List~Product~
        -parseLine(String[]): Product
    }
    class CSVParser~Category~ {
        +parse(String): List~Category~
        -parseLine(String[]): Category
    }
    class ConcreteProductProvider {
        -Reader reader
        -Parser~Product~ parser
        +getEntities(): List~Product~
    }
    class ConcreteCategoryProvider {
        -Reader reader
        -Parser~Category~ parser
        +getEntities(): List~Category~
    }

    %% Визуализация/отправка
    class DataBaseRenderer {
        -JdbcTemplate jdbc
        -Provider~Product~ productProvider
        -Provider~Category~ categoryProvider
        +render(): void
        +insertProduct(Product): void
        +insertCategory(Category): void
    }

    %% Spring‑конфигурация и AOP
    class PropertyProvider {
        +getProductFileName(): String
        +getCategoryFileName(): String
    }
    class Config {
        +productReader(): Reader
        +categoryReader(): Reader
        +productParser(): Parser~Product~
        +categoryParser(): Parser~Category~
        +productProvider(): Provider~Product~
        +categoryProvider(): Provider~Category~
        +renderer(): Renderer
    }
    class TimeAround {
        +invoke(MethodInvocation): Object
    }
    class TimeAroundPointCut {
        +matches(Method, Class~?~): boolean
    }

    %% Отношения
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser~Product~
    Parser <|.. CSVParser~Category~
    Provider <|.. ConcreteProductProvider
    Provider <|.. ConcreteCategoryProvider
    Renderer <|.. DataBaseRenderer

    ConcreteProductProvider --> ResourceFileReader
    ConcreteProductProvider --> CSVParser~Product~
    ConcreteProductProvider --> Product

    ConcreteCategoryProvider --> ResourceFileReader
    ConcreteCategoryProvider --> CSVParser~Category~
    ConcreteCategoryProvider --> Category

    DataBaseRenderer --> JdbcTemplate
    DataBaseRenderer --> ConcreteProductProvider
    DataBaseRenderer --> ConcreteCategoryProvider

    ResourceFileReader --> PropertyProvider
    CSVParser~Product~ --> Product
    CSVParser~Category~ --> Category

    Config ..> ResourceFileReader
    Config ..> CSVParser~Product~
    Config ..> CSVParser~Category~
    Config ..> ConcreteProductProvider
    Config ..> ConcreteCategoryProvider
    Config ..> DataBaseRenderer
    Config ..> TimeAround
    Config ..> TimeAroundPointCut
```

## Выводы
