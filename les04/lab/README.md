# Отчет о лаботаротоной работе

## Цель работы

Изучить механизмы конфигурации Spring-приложений с помощью аннотаций, научиться использовать `@Component`, `@Value`, события жизненного цикла бинов, AOP и подключение ресурсов. Закрепить умения по модульной архитектуре, внедрению зависимостей и применению аспектно-ориентированного программирования.

## Выполнение работы


### 1. Копирование предыдущей работы

Результат выполнения лабораторной работы №1 был скопирован в директорию `les04/lab/`.

---

### 2. Конфигурирование с помощью @Component

Вся ручная конфигурация заменена на автоматическое сканирование компонентов. Классы `Reader`, `ProductProvider`, `Renderer` и их реализации аннотированы `@Component`.

```java
@Component
public class ResourceFileReader implements Reader { ... }

Контекст запускается через:
ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

3. Загрузка конфигурации из application.properties

Добавлен файл src/main/resources/application.properties:

input.file.name=product.csv

В классе ResourceFileReader используется:

@Value("${input.file.name}")
private String fileName;

4. Реализация HTMLTableRenderer
Создан класс HTMLTableRenderer, который сохраняет HTML-таблицу в файл output.html. Он выбран активной реализацией через аннотацию @Primary.

@Component
@Primary
public class HTMLTableRenderer implements Renderer { ... }

5. Использование @PostConstruct
В ResourceFileReader добавлен метод инициализации:

@PostConstruct
public void afterInit() {
    System.out.println("ResourceFileReader инициализирован: " + LocalDateTime.now());
}

6. Замер времени парсинга с помощью AOP
Создан аспект:

@Aspect
@Component
public class TimingAspect {
    @Around("execution(* ru.bsuedu.cad.lab.reader.ResourceFileReader.read(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result = joinPoint.proceed();
        long end = System.nanoTime();
        System.out.println("Время выполнения чтения файла: " + (end - start) / 1_000_000 + " мс");
        return result;
    }
}
7. Запуск через gradle run
Команда gradle run успешно запускает приложение. В консоли отображаются:

ResourceFileReader инициализирован: 2025-05-07T19:04:03.854298
Время выполнения метода read(): 1 мс
HTML-файл успешно создан: output.html

8. UML-диаграмма классов

    interface Reader {
        +String read()
    }

    class ResourceFileReader {
        +read()
    }

    Reader <|.. ResourceFileReader

    interface ProductProvider {
        +List<Product> getProducts()
    }

    class ConcreteProductProvider {
        +getProducts()
    }

    ProductProvider <|.. ConcreteProductProvider

    class Product {
        -String name
        -double price
        +getName()
        +getPrice()
    }

    interface Renderer {
        +void render()
    }

    class ConsoleTableRenderer {
        +render()
    }

    class HTMLTableRenderer {
        +render()
    }

    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer

    class App {
        +main()
    }

    App --> Renderer

## Выводы
В ходе лабораторной работы:
    Освоены аннотации Spring (@Component, @Value, @PostConstruct, @Aspect);
    Реализована конфигурация через файл application.properties;
    Добавлена альтернатива рендеринга — HTML-таблица;
    Освоены инструменты AOP для замера производительности;
    Приложение корректно запускается через Gradle и завершает работу без ошибок.