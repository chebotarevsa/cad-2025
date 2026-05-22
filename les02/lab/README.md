# Отчет о лабораторной работе

## Цель работы

Создать базовое консольное приложение на Java с использованием Gradle и Spring Framework.
Приложение должно читать данные о товарах из CSV-файла и выводить их в консоль в виде таблицы.

## Выполнение работы

В директории les02/lab создан Gradle-проект product-table со следующими параметрами:
- тип проекта: Application;
- язык: Java;
- версия Java: 17;
- DSL: Kotlin;

В файл app/build.gradle.kts добавлена зависимость implementation("org.springframework:spring-context:6.2.2")


CSV-файл product.csv размещен в директории app/src/main/resources.

Реализованы классы:
- Product - модель товара;
- Reader и ResourceFileReader` - чтение CSV-файла из ресурсов;
- Parser и CSVParser - разбор CSV-данных;
- ProductProvider и ConcreteProductProvider - получение списка товаров;
- Renderer и ConsoleTableRenderer - вывод таблицы товаров в консоль;
- AppConfig - Java-конфигурация Spring;
- App - точка входа приложения.

## Выводы

В ходе работы был создан каркас Java-приложения на Gradle, подключен Spring Context,
реализована Java-конфигурация Spring и применено внедрение зависимостей между компонентами приложения.
