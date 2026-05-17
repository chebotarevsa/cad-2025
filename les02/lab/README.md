# Лабораторная работа №1. Gradle. Базовое приложение Spring

**Выполнил:** Хайруллин Эльдар Ринатович, группа 12002453

## Цель работы
Изучить основы сборки Java-проекта с помощью Gradle, создать каркас консольного приложения на Spring Framework с использованием Java-конфигурации, реализовать загрузку данных из CSV-файла и их вывод в виде таблицы.

## Используемые инструменты
- JDK 17 (Temurin 17.0.14)
- Gradle 8.12
- Spring Context 6.2.2
- IntelliJ IDEA / командная строка

## Структура проекта
```
les02/lab
├── app
│ ├── build.gradle.kts
│ └── src
│ ├── main
│ │ ├── java/ru/bsuedu/cad/lab
│ │ │ ├── Main.java
│ │ │ ├── AppConfig.java
│ │ │ ├── Product.java
│ │ │ ├── Reader.java
│ │ │ ├── ResourceFileReader.java
│ │ │ ├── Parser.java
│ │ │ ├── CSVParser.java
│ │ │ ├── ProductProvider.java
│ │ │ ├── ConcreteProductProvider.java
│ │ │ ├── Renderer.java
│ │ │ └── ConsoleTableRenderer.java
│ │ └── resources
│ │ └── products.csv
│ └── test/...
└── settings.gradle.kts
```

## Диаграмма классов
```mermaid
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

## Выполнение работы

### 1. Настройка проекта
- Установлен JDK 17 и Gradle 8.12 согласно инструкциям.
- В папке `les02/lab` выполнена команда:
  gradle init --type java-application --package ru.bsuedu.cad.lab --project-name product-table --java-version 17 --dsl kotlin --test-framework junit-jupiter
- В app/build.gradle.kts добавлена зависимость spring-context:6.2.2 и настройка кодировки компиляции UTF-8.

### 2. Разработка классов
- Реализован класс Product с полями согласно диаграмме.
- Создан интерфейс Reader и его реализация ResourceFileReader, читающая CSV-файл из ресурсов через ClassPathResource.
- Создан интерфейс Parser и CSVParser, преобразующий строку в список Product, с пропуском заголовка и обработкой дат через SimpleDateFormat.
- Написан ConcreteProductProvider, связывающий Reader и Parser.
- Реализован ConsoleTableRenderer, выводящий таблицу с отформатированными колонками, включая даты в виде yyyy-MM-dd HH:mm.
- Создан конфигурационный класс AppConfig с определением бинов, связывающих компоненты через внедрение зависимостей.
- В Main классе запускается контекст Spring и вызывается renderer.render().

### 3. Решение проблемы с кодировкой кириллицы в консоли Windows
Изначально русские символы отображались как ??? из-за несоответствия кодировок.
Файл products.csv был сохранен в UTF-8.
В Main.java добавлена строка:
	System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
Шрифт консоли изменён на Consolas, кодовая страница переключена на UTF-8 (chcp 65001).
После этого таблица отображается корректно.

## Результат работы
![Результат работы](console_output.png)

## Инструкция по запуску
- Перейти в папку les02/lab.
- Выполнить gradle run.
- В консоли должна отобразиться таблица с товарами.

## Ответы на контрольные вопросы
1. Spring. Определение, назначение, особенности
Spring – это универсальный фреймворк для разработки Java-приложений, предоставляющий инфраструктурную поддержку (IoC, DI, AOP, управление транзакциями, доступ к данным, веб и др.). Особенности: лёгковесность, модульность, интеграция с множеством технологий.
2. Проблемы ручной сборки приложений
Ручное управление зависимостями, сложность компиляции и тестирования, отсутствие стандартизации, повторяемости сборки, сложность работы в команде.
3. Известные системы автоматической сборки
- Maven – декларативная сборка, централизованное управление зависимостями, стандартный жизненный цикл.
- Gradle – гибкая сборка на Groovy/Kotlin DSL, инкрементальная компиляция, высокая производительность.
- Ant – императивная сборка (XML), гибкость, отсутствие стандартных соглашений.
4. Типовая структура Java проекта
src/main/java – исходный код, src/main/resources – ресурсы, src/test/java – тесты, build.gradle/pom.xml – сценарий сборки.
5. Типы зависимостей в Gradle
implementation (компиляция и выполнение), testImplementation (только тесты), runtimeOnly (только во время выполнения), compileOnly (только компиляция) и другие.
6. Принцип инверсии управления (IoC)
IoC – передача управления созданием и связыванием объектов внешнему контейнеру (Spring). Применяется для уменьшения связанности и упрощения тестирования.
7. Отличие IoC от DI
IoC – общий принцип передачи управления, а DI (внедрение зависимостей) – один из способов его реализации, когда зависимости предоставляются объекту извне.
8. Принципы инверсии управления
- Голливудский принцип: «Не звоните нам, мы сами позвоним» – компонент не управляет потоком выполнения.
- Внедрение зависимостей (через конструктор, сеттеры, поля).
- Сервис-локатор (менее предпочтителен).
9. Сцепление (Coupling) и связность (Cohesion)
Сцепление – степень зависимости между модулями (должно быть низким). Связность – насколько элементы внутри модуля связаны по смыслу (должна быть высокой).
10. Какой принцип внедрения зависимости желательно использовать и почему?
Рекомендуется внедрение через конструктор, так как оно гарантирует неизменяемость зависимости, упрощает тестирование и делает явными обязательные зависимости класса.