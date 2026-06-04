# Отчёт о лабораторной работе 1. Gradle. Базовое приложение Spring

## Цель работы

Создать каркас консольного Spring-приложения «Магазин товаров для животных», настроить сборку Gradle, реализовать загрузку и отображение каталога товаров из CSV-файла с использованием инверсии управления и внедрения зависимостей.

## Выполнение работы

1. Установлены JDK 17 и Gradle 8.x (через Gradle Wrapper проекта).
2. Создан Gradle-проект `product-table` (Java 17, Kotlin DSL, JUnit Jupiter) в каталоге `les02` с пакетом `ru.bsuedu.cad.lab`.
3. В зависимости приложения добавлена библиотека `org.springframework:spring-context:6.2.2`.
4. Реализовано консольное приложение с Java-конфигурацией Spring (`AppConfiguration`):
   - `ResourceFileReader` — чтение `product.csv` из `src/main/resources`;
   - `CSVParser` — разбор CSV в список сущностей `Product`;
   - `ConcreteProductProvider` — предоставление списка товаров;
   - `ConsoleTableRenderer` — вывод таблицы в консоль.
5. Точка входа — класс `App`, по образцу `AppWithSpringJava` из демо-лекции: создаётся `AnnotationConfigApplicationContext`, из контекста получается бин `renderer` и вызывается `render()`.

### Структура проекта

```
les02/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── java/ru/bsuedu/cad/lab/
│       │   ├── App.java
│       │   ├── AppConfiguration.java
│       │   ├── Product.java
│       │   ├── Reader.java, Parser.java, ProductProvider.java, Renderer.java
│       │   └── impl/
│       │       ├── ResourceFileReader.java
│       │       ├── CSVParser.java
│       │       ├── ConcreteProductProvider.java
│       │       └── ConsoleTableRenderer.java
│       └── resources/product.csv
└── lab/README.md
```

### Запуск

Из корня `les02`:

```bash
./gradlew run
```

Приложение выводит таблицу товаров и завершается с кодом 0.

### Тесты

```bash
./gradlew test
```

## Выводы

В ходе работы освоены базовая настройка Gradle-проекта, подключение Spring Context и конфигурирование бинов через `@Configuration` и `@Bean`. Разделение ответственности по интерфейсам (`Reader`, `Parser`, `ProductProvider`, `Renderer`) снижает сцепление компонентов и упрощает замену реализаций. Spring-контейнер управляет созданием объектов и связыванием зависимостей через конструкторы, что соответствует принципу внедрения зависимостей.
