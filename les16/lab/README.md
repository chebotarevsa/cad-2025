# Отчет о лаботаротоной работе

## Цель работы

Реализовать модульное (юнит-) и интеграционное тестирование сервиса создания заказов в приложении Spring Boot.

## Выполненные шаги

Скопирован результат лабораторной работы №6 в /les14/lab/.

Добавлены зависимости:

spring-boot-starter-test

mockito-core

jakarta.persistence-api

Настроен JaCoCo:

plugins {
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

Написан Unit-test для OrderService c Mock--зависимостями. Тесты на успех/ошибку.

Тесты прошли. Сформирован HTML/репорт JaCoCo: build/reports/jacoco/test/html/index.html

Добавлен @DataJpaTest для интеграционных тестов OrderService + Repository

Тесты протестировали:

создание заказа из базы

ошибки при несуществующих данных

Тесты выполнены. Все прошли. JaCoCo покрытие: 80%+ кода OrderService

Оформлен README.md + UML-диаграмма

classDiagram
    class OrderService {
        +createOrder()
        +getOrders()
        +deleteOrder()
    }

    class OrderRepository
    class CustomerRepository
    class ProductRepository

    OrderService --> OrderRepository
    OrderService --> CustomerRepository
    OrderService --> ProductRepository

##  Вывод

В ходе работы проект был обогащён тестами: модульными и интеграционными. Было достигнуто высокое покрытие кода, а сам код проверен на стабильность и ошибки.




