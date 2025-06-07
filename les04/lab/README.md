
``` mermaid
classDiagram
    note "Товары для зоомагазина — Spring + AOP"
    
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

Spring предоставляет три основных способа конфигурирования ApplicationContext:

    XML-конфигурация
    Определение бинов в applicationContext.xml. Устаревающий способ, но всё ещё поддерживается.

    Java-конфигурация (@Configuration)
    Использование классов с аннотацией @Configuration и методов @Bean.

    Аннотационное конфигурирование (Component Scan)
    Использование @Component, @Service, @Repository, @Controller с автоматическим сканированием классов (@ComponentScan).

2. Стереотипные аннотации

Стереотипы — это аннотации, маркирующие класс как компонент Spring-контейнера:

    @Component — базовая аннотация для любого Spring-бина.

    @Service — логика бизнес-уровня.

    @Repository — слой доступа к данным (DAO), с обработкой исключений.

    @Controller — компонент для обработки HTTP-запросов (в MVC).

    @RestController — @Controller + @ResponseBody, для REST API.

Используются для автоматического создания и управления бинами.
3. Инъекция зависимостей (DI)

Spring поддерживает автоматическое связывание (Autowiring) зависимостей:
Виды:

    @Autowired — по типу, ищет подходящий бин.

        Можно использовать на конструкторе, поле, или сеттере.

    @Qualifier("beanName") — уточнение, какой бин использовать, если несколько кандидатов.

    @Inject (из javax.inject) — аналог @Autowired, без поддержки required=false.

    @Resource(name="beanName") — из JSR-250, связывает по имени.

4. Внедрение простых параметров

Для внедрения значений из application.properties используют:

@Value("${property.name}")
private String property;

Или через конструктор/сеттер. Пример:

app.title=Магазин товаров

@Value("${app.title}")
private String title;

5. Внедрение параметров с помощью SpEL (Spring Expression Language)

SpEL позволяет использовать выражения в @Value:

@Value("#{2 * 3}")
private int result;

@Value("#{systemProperties['user.name']}")
private String userName;

@Value("#{T(Math).PI}")
private double pi;

Также можно обращаться к другим бинам:

@Value("#{someBean.someProperty}")
private String valueFromBean;

6. Режимы получения бинов

    Singleton (по умолчанию): один экземпляр на контейнер Spring.

    Prototype: новый экземпляр каждый раз при запросе.

    Request: один бин на HTTP-запрос (в Web-приложениях).

    Session: один бин на HTTP-сессию.

    Application: один бин на ServletContext.

    Websocket: один бин на WebSocket-сессию.

@Scope("prototype")

7. Жизненный цикл бинов

    Создание (через конструктор)

    Установка зависимостей (@Autowired, @Value)

    @PostConstruct метод

    Готов к использованию

    Удаление / завершение

    @PreDestroy метод

Можно также реализовать интерфейсы:

    InitializingBean (afterPropertiesSet())

    DisposableBean (destroy())

8. Что такое AOP?

AOP (Aspect-Oriented Programming) — аспектно-ориентированное программирование, позволяет добавлять функциональность (например, логирование, безопасность, транзакции) без изменения кода основного класса.
9. Типы AOP в Spring

    Spring AOP (на основе прокси) — используется по умолчанию, работает только с Spring Bean'ами.

    AspectJ (компиляторный/байткодный уровень) — более мощный, но требует дополнительной настройки и зависимости.

10. Виды Advice (советов)

    @Before — выполняется до метода.

    @After — выполняется после метода (в любом случае).

    @AfterReturning — после успешного завершения метода.

    @AfterThrowing — при выбрасывании исключения.

    @Around — обёртка вокруг метода (позволяет управлять вызовом).

11. Виды Pointcut

Pointcut определяет где применять Advice:

    execution(...) — методы (наиболее распространённый).

    within(...) — все методы внутри класса/пакета.

    this(...) / target(...) — по типу прокси/объекта.

    args(...) — методы с определёнными аргументами.

    @annotation(...) — методы с аннотацией.

Пример:

@Around("execution(* com.example.service.*.*(..))")

| Особенность        | Spring AOP                 | AspectJ                      |
| ------------------ | -------------------------- | ---------------------------- |
| Основан на         | Прокси (JDK/CGLIB)         | Байткод или компиляция       |
| Производительность | Ниже                       | Выше                         |
| Гибкость           | Только методы Spring-бинов | Любой код, включая поля      |
| Простота           | Легче в настройке          | Требует AspectJ-compiler     |
| Использование      | В большинстве проектов     | В высоконагруженных системах |
