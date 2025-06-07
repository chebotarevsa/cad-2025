
``` mermaid
classDiagram
    
    App --> Renderer
    App --> PerformanceAspect

    Renderer <|.. HTMLTableRenderer
    HTMLTableRenderer o-- ProductProvider

    ProductProvider o-- Reader
    ProductProvider o-- Parser

    Reader <|.. ResourceFileReader
    Parser <|.. ProductCSVParser

    ResourceFileReader ..> application.properties : @Value
    ResourceFileReader --> File : reads

    PerformanceAspect ..> ProductCSVParser : @Around parse()

    ProductCSVParser ..> Product

    class App {
        +main(String[] args)
    }

    class Renderer {
        +void render()
    }
    <<interface>> Renderer

    class HTMLTableRenderer {
        -ProductProvider provider
        +void render()
    }

    class ProductProvider {
        +List~Product~ getProducts()
    }

    class Reader {
        +String read()
    }
    <<interface>> Reader

    class ResourceFileReader {
        +String read()
        +@PostConstruct init()
    }

    class Parser {
        +List~Product~ parse(String)
    }
    <<interface>> Parser

    class ProductCSVParser {
        +List~Product~ parse(String)
    }

    class PerformanceAspect {
        +Object measureTime(ProceedingJoinPoint)
    }

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
```

1. Виды конфигурирования ApplicationContext

    XML-конфигурация — настройка контекста с помощью XML-файла (обычно applicationContext.xml).

    Java-конфигурация (Java-based config) — использование классов с аннотацией @Configuration и методами с @Bean.

    Аннотационная конфигурация (Annotation-based config) — компоненты конфигурируются с помощью аннотаций (@Component, @Service, и т.д.).

    Смешанная конфигурация — комбинация XML, Java и аннотаций.

2. Стереотипные аннотации

Используются для автоматического обнаружения и регистрации бинов в Spring-контейнере:

    @Component — общий компонент.

    @Service — слой сервиса.

    @Repository — слой DAO, автоматически обрабатывает исключения.

    @Controller — слой контроллеров в MVC.

    @RestController — то же, что @Controller, но автоматически добавляет @ResponseBody.

3. Инъекция зависимостей: виды автоматического связывания (autowiring)

    byType — связывает по типу (например, если есть только один бин Car, он будет внедрён).

    byName — связывает по имени бина.

    constructor — связывает через конструктор.

    @Autowired — аннотация, которая по умолчанию связывает по типу.

        Может применяться к полям, конструкторам, методам.

    @Inject (из JSR-330) — аналог @Autowired.

    @Qualifier — уточнение, какой именно бин внедрять, если их несколько одного типа.

4. Внедрение простых параметров в бин

    Через аннотацию @Value:

@Value("${app.name}")
private String appName;

    Источник: application.properties или application.yml.

5. Внедрение параметров с помощью SpEL (Spring Expression Language)

Позволяет использовать выражения:

@Value("#{2 * 2}")
private int result;

@Value("#{myBean.someProperty}")
private String propFromBean;

6. Режимы получения бинов

    singleton (по умолчанию) — один экземпляр бина на контейнер.

    prototype — каждый раз создаётся новый экземпляр.

    request — один бин на HTTP-запрос (только в веб-приложениях).

    session — один бин на HTTP-сессию.

    application — один бин на ServletContext.

7. Жизненный цикл бинов

    Создание объекта.

    Установка зависимостей.

    Вызов методов @PostConstruct или InitializingBean.afterPropertiesSet().

    Использование бина.

    Завершение — вызов @PreDestroy или DisposableBean.destroy().

8. Что такое АОП? Основные понятия AOP

АОП (Aspect-Oriented Programming) — парадигма, позволяющая отделить сквозную логику (например, логирование, безопасность) от основной бизнес-логики.

    Aspect — модуль сквозной логики.

    Advice — действие, выполняемое в определённой точке.

    Join point — точка в приложении, где можно применить Advice (например, вызов метода).

    Pointcut — выражение, определяющее, где именно применять Advice.

    Weaving — процесс применения аспектов.

9. Типы АОП в Spring

    Spring AOP — использует динамические прокси на основе интерфейсов (JDK) или классов (CGLIB).

    AspectJ — более мощный фреймворк, поддерживает компиляцию аспектов на уровне байткода.

10. Виды Advice

    @Before — перед вызовом метода.

    @After — после вызова метода (всегда).

    @AfterReturning — после успешного выполнения.

    @AfterThrowing — после выбрасывания исключения.

    @Around — оборачивает метод, позволяет контролировать выполнение (до и после).

11. Виды Pointcut

    execution — определяет метод(ы) по сигнатуре.

    within — ограничивает область применения аспектов определённым классом или пакетом.

    args — применяет аспект к методам с определёнными аргументами.

    this / target — указывает на объект, через который вызван метод.

Пример:
@Pointcut("execution(* com.example.service.UserService.getUserById(..))")
public void getUserByIdMethod() {}

12. Чем Spring AOP отличается от AspectJ
Характеристика		Spring AOP		AspectJ

Основа			Прокси (JDK/CGLIB)	Компиляция байткода
Производительность	Ниже			Выше
Поддержка JoinPoint	Только вызов методов	Любые JoinPoints (конструкторы и т.д.)
Простота		Легче в использовании	Требует дополнительной настройки