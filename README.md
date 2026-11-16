# Разработка кроссплатформенных приложений

## Лекции

1. 🟢 [Обзор Spring Framework. Системы автоматической сборки](./les01/README.md)
2. 🟢 [Инверсия управления (IoC) и внедрение зависимостей (DI) в Spring](./les02/README.md)
3. 🟢 [Конфигурирование и начальная загрузка в Spring](./les03/README.md)
4. 🟢 [Ведение в AOP средствами Spring](./les04/README.md)
5. 🟢 [Поддержка JDBC в Spring](./les05/README.md)
6. 🟢 [Технология ORM. Спецификация JPA2. Entity](./les06/README.md)
7. 🟢 [Управления объектами. Язык запросов JPQL](./les07/README.md)
8. 🟢 [Spring Data.Управление транзакциями](./les08/README.md)
9. 🟢 [Сервлеты. Разработка Web-приложений](./les09/README.md)
10. 🟢 [REST. Маршализация XML и JSON.](./les10/README.md)
11. 🟢 [Spring MVC.](./les11/README.md)
12. 🟢 [Spring MVC.Технологии представления. Thymeleaf](./les12/README.md)
13. 🟢 [Безопасность Web-приложений. Spring security](./les13/README.md)
14. 🟢 [Тестирование приложений.  Unit тестирование](./les14/README.md)
15. 🟢 [Тестирование приложений. Интеграционное тестирование](./les15/README.md)
16. 🟢 [Введение в Spring Boot](./les16/README.md)
17. 🟢 [Low-код платформы на основе фреймворка Spring. Jmix.](./les17/README.md)
18. 🟢 [Контейнеризация Spring приложений. Docker.](./les18/README.md)

## Лабораторные работы (семестр 1)

1. 🟢 [Gradle. Внедрение зависимостей](./les02/lab.md)
2. 🟢 [Конфигурирование приложений Spring](./les04/lab.md)
3. 🟢 [Технологии работы с базами данных. JDBC](./les06/lab.md)
4. 🟢 [Технологии работы с базами данных. JPA. Spring Data](./les08/lab.md)
5. 🟢 [Разработка  Web-приложений с использованием сервлетов](./les10/lab.md)
6. 🟢 [Разработка Web-приложений с использованием технологии Spring MVC](./les12/lab.md)
7. 🟢 [Spring security.Basic Authentication](./les14/lab.md)
8. 🟢 [Основы тестирования](./les16/lab.md)


## Лабораторные работы (семестр 2)
1. 🟢 [Создание проекта Spring Boot](./sem2/lab1/) · [Отчёт в Google Colab](https://colab.research.google.com/github/kir1lka/cad-2025/blob/main/sem2/lab1/rep.ipynb)
2. 🟢 [Создание проекта Spring MVC](./sem2/lab2/) · [Отчёт в Google Colab](https://colab.research.google.com/github/kir1lka/cad-2025/blob/main/sem2/lab2/rep.ipynb)
3. 🟢 [Spring Security](./sem2/lab3/) · [Отчёт в Google Colab](https://colab.research.google.com/github/kir1lka/cad-2025/blob/main/sem2/lab3/rep.ipynb)
4. 🟢 [Spring и PostgreSQL](./sem2/lab4/) · [Отчёт в Google Colab](https://colab.research.google.com/github/kir1lka/cad-2025/blob/main/sem2/lab4/rep.ipynb)
5. 🟢 [Создание клиента](./sem2/lab5/)
6. 🟢 [Создание клиента Android](./sem2/lab6/)

В семестре 2 выполнены работы №1–4. Отчёты содержат пояснения, примеры Java-кода и скриншоты в текстовых ячейках. Для запуска проектов на ПК требуются Java 17 и Node.js; для PostgreSQL в работе №4 используется Docker Desktop.

## Правила выполнения и сдачи лабораторных

1. Создайте учетную запись на сайте <https://github.com/>
2. Выполните форк репозитория  <https://github.com/Nikita-Puzyrev/cad-2025/>
3. Установите и сконфигурируйте git на локальной машине
4. Клонируйте репозиторий из **своей учетной записи** на локальную машину
5. Выполните лабораторную работу в директории lesXX/lab, где XX номер занятия. Во избежания проблем при обновлении репозитория, файлы в других директориях не меняйте.
6. Заполните отчет о выполненной работе в виде фала README.MD в корне директории lesXX/lab
7. После выполнение работы выполните commit и push в своей репозиторий.
8. Создайте pull request со своими изменениями для репозитория  <https://github.com/Nikita-Puzyrev/cad-2025/> в названии pull request-а укажите норме группы и ФИО (например, 12002500 Иванов Д.А)
9. В системе "Пегас" приложите файл README.MD
10. Работу необходимо защитить. Работа оценивается по 100 больной системе. Для допуска к зачету или экзамену необходимо набрать 400 балов из 800.

## Критерии итоговых оценок

|Балы|Оценка|
|---|--|
| 720 - 800 | Отлично |
| 560 - 720 | Хорошо |
| 400 - 560 | Удовлетворительно |
| менее 400 | Неудовлетворительно/Не допущен |

## Вопросы к экзамену (семестр 1)

1. Какие цели и преимущества использования Spring Framework в разработке Java-приложений?
2. Что такое IoC и DI? Почему эти подходы важны в Spring?
3. Как устроен жизненный цикл компонента (бина) в Spring-приложении?
4. Какие существуют способы конфигурирования Spring-приложений? В чём их различие?
5. Что такое AOP и как оно применяется в Spring?
6. В чём особенности работы с JDBC через Spring?
7. Что такое ORM и какие технологии Spring поддерживает для работы с базами данных?
8. В чём преимущества использования Spring Data при доступе к данным?
9. Как в Spring реализуется управление транзакциями?
10. Что такое архитектурная модель MVC? Как она реализована в Spring MVC?
11. Как осуществляется маршрутизация HTTP-запросов в Spring MVC?
12. Как устроена структура типичного Spring MVC-приложения?
13. Какие технологии представления можно использовать в связке с Spring MVC?
14. Какие возможности предоставляет Spring Security? Как он настраивается?
15. Какие типы аутентификации и авторизации поддерживает Spring Security?
16. Как реализуется тестирование компонентов Spring-приложений (юнит-тесты и интеграционные тесты)?
17. Какие подходы и инструменты используются для тестирования Spring-приложений? В чём различие между юнит- и интеграционными тестами?
18. Что такое Spring Boot и чем он отличается от классического Spring?
19. Какие возможности предоставляет Jmix как надстройка над Spring?
20. Как происходит контейнеризация Spring-приложений? Какие инструменты используются?

## Литература

1. [Pro Spring 6. An In-Depth Guide to the Spring Framework. Iuliana Cosmina, Rob Harrop, Chris Schaefer, and Clarence Ho. Apress Berkeley, CA 2023](https://link.springer.com/book/10.1007/978-1-4842-8640-1)
2. [Spring in Action, Sixth Edition.Craig Walls, Manning 2022](https://www.manning.com/books/spring-in-action-sixth-edition)
3. [Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/index.html)
4. [Spring 5 для профессионалов, Хо Кларенс, Козмина Юлиана, Шефер Крис, Харроп Роб, Диалектика-Вильямс, 2019](https://www.williamspublishing.com/Books/978-5-907114-07-4.html)
