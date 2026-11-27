# Лекция 3. Внедрение зависимостей в Spring

Разработка Spring-состоит их двух этапов:

+ Создание компонентов (java-классов) реализующих бизнес логику приложения.
+ Конфигурирование контейнера  - помещение инстанций компонентов в контейнер внедрения зависимостей Spring.

Объект, который управляется контейнером Spring IoC (Inversion of Control) называется **Spring Bean**. Бины создаются, инициализируются, управляются и уничтожаются контейнером Spring в зависимости от их конфигурации.
  
Ядром контейнера внедрения зависимостей в Spring служит интерфейс [**BeenFactory**](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/BeanFactory.html), который отвечает за управление компонентами Spring Beans, в том числе их зависимостями и жизненными циклами. Термин компонент **Spring Веап** употребляется в Spring для обозначения любого компонента, управляемого контейнером.
За конфигурирование **Spring Веап** отвечает реализация интерфейса [**BeenDefinition**](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/config/BeanDefinition.html).  

Интерфейс ApplicationContext служит расширением интерфейса BeanFactory в Spring. Помимо услуг no внедрению зависимостей, интерфейс Application Context предоставляет такие услуги, как транзакции и АОП, источник сообщений для интернационализации (i18n), обработка событий в приложениях и пр. Это интерфейс к которым мы будем работать при разработки приложения.

## 🔴 Конфигурирование ApplicationContext

Виды конфигурирования ApplicationContext:

1. XML-конфигурация
   + Используется файл applicationContext.xml
   + Бины определяются с помощью XML-тегов
   + Устаревший метод, но иногда используется в легаси-коде

2. Конфигурация на основе аннотаций
   + Использует аннотации @Component, @Service, @Repository, @Controller, @Bean, @Configuration
   + Требует включения Component Scan (@ComponentScan)
   + Более гибкий и современный метод

3. Конфигурация на основе Java-классов (@Configuration)
   + Использует аннотации @Configuration и @Bean
   + Позволяет более точно управлять созданием бинов

## 🔴 Конфигурация на основе Java-классов

C Java конфигурацией мы познакомились на прошлой лекции.

Документация - <https://docs.spring.io/spring-framework/reference/core/beans/java/configuration-annotation.html>

``` java
@Configuration
public class MessageConfiguration {

    @Bean
    MessageProvider  provider() {
        return new MessageProviderImpl();
    } 
    
    @Bean
    MessageRenderer renderer() {
        MessageRenderer renderer = new MessageRendererImpl();
        renderer.setMessageProvider(provider());
        return renderer;
    } 
}
```

Недостатки:

+ Возрастает объем кода - В Java-конфигурации нужно вручную создавать методы @Bean, что увеличивает объем кода.
+ Трудности с масштабированием - С аннотациями код остается более модульным, так как конфигурация распределена по классам.

Использование конфигурации

+ Когда нужно четко контролировать создание бинов и их зависимости.
+ Когда приложение сильно зависит от внешних конфигураций и профилей.
+ Когда хочется избежать магии аннотаций и упростить отладку.
+ Когда проект очень большой, и важно четко структурировать конфигурацию.

## 🔴 Конфигурация на основе аннотаций

### 🟢 Определение компонентов Spring

Другой способ объявления бинов — это аннотирование классов напрямую с помощью стереотипных аннотаций.
Документация - <https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html>

**Стереотипные аннотации**— это аннотации Spring, которые указывают, что класс представляет собой определенный тип компонента (бин) и должен быть автоматически зарегистрирован в Spring-контексте.

| Аннотация        | Описание | Где применяется |
|-----------------|----------|----------------|
| `@Component`    | Базовая аннотация для любого Spring-бина. | Любой класс, который должен управляться Spring. |
| `@Service`      | Аннотация для сервисного слоя, содержащего бизнес-логику. | Классы с бизнес-логикой (например, UserService). |
| `@Repository`   | Указывает, что класс отвечает за работу с базой данных. Позволяет Spring автоматически обрабатывать исключения, связанные с БД. | DAO-классы, взаимодействующие с базой (например, UserRepository). |
| `@Controller`   | Используется в MVC-приложениях для обработки HTTP-запросов и возврата HTML-страниц. | Классы-контроллеры (например, HomeController). |
| `@RestController` | Совмещает `@Controller` и `@ResponseBody`, используется для REST API, автоматически возвращает JSON/XML. | REST-контроллеры (например, ApiController). |

Сконфигурируем проект из прошлой лекции с помощью аннотаций.

```java
@Component("provider")
public class ProviderImpl  implements Provider{

    @Override
    public String getMessage() {
        return "Message from provider";
    }

}
```

```java
@Component("renderer")
public class RendererImpl implements Renderer {

    private Provider provider;

    @Override
    public void render() {
        System.out.println(provider.getMessage());
    }

}
```

Однако, несмотря на аннотации, Spring не будет автоматически искать компоненты и добавлять из в контейнер.  Его необходимо "заставить" выполнить поиск с помощью аннотации @ComponentScan.

``` java

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cap.component")
public class Config {

}
```

При запуске приложения в таком виде мы получим ошибку

``` bash
> Task :app:run FAILED
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "ru.bsuedu.cap.component.Provider.getMessage()" because "this.provider" is null
        at ru.bsuedu.cap.component.impl.RendererImpl.render(RendererImpl.java:16)
        at ru.bsuedu.cap.component.App.main(App.java:14)
```

Ошибка вызвана тем, мы не инициализируем поле provider в классе RendererImpl.

### 🟢 Инъекция зависимостей

Документация - <https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html>

#### Ручное связывание

Получить оба бина из контейнера и установить provider вручную


```java

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
         Renderer mr = ctx.getBean("renderer", Renderer.class);
         Provider pr = ctx.getBean("provider", Provider.class);
         mr.setProvider(pr);
         mr.render();
          }
    }

```

Никогда так не делайте 🤮

---

#### Автоматическое связывание через методы установки (Setter Injection)

Setter Injection — это способ внедрения зависимостей в Spring через сеттер-методы (методы установки значений).

Spring автоматически передает нужные зависимости в объект, вызывая соответствующие setter-методы помеченный аннотацией @Autowired.

``` java
@Component("renderer")
public class RendererImpl implements Renderer {

    private Provider provider;

    @Autowired
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        System.out.println(provider.getMessage());
    }

}
```
---

#### Автоматическое связывание через конструктор

Constructor Injection — это способ внедрения зависимостей через конструктор класса. Этот метод считается наиболее предпочтительным 😍, так как:

+ Позволяет объявить зависимости как final, исключая их изменение после создания объекта.
+ Гарантирует, что объект создастся только с необходимыми зависимостями.
+ Упрощает написание тестов (легко внедрять мок-зависимости).

```java
@Component("renderer")
public class RendererImpl implements Renderer {

    private final Provider provider;


    @Autowired
    public RendererImpl(Provider provider) {
        this.provider = provider;
    }



    @Override
    public void render() {
        System.out.println(provider.getMessage());
    }

}
```

---

#### Автоматическое связывание через поле

Field Injection — это способ внедрения зависимостей через прямую аннотацию @Autowired на поле без использования сеттеров или конструктора.

``` java
@Component("renderer")
public class RendererImpl implements Renderer {

    @Autowired
    private  Provider provider;


    @Override
    public void render() {
        System.out.println(provider.getMessage());
    }

}
```

### 🟢 Наименование бинов. Правила автосвязывания

#### Наименование бинов

Каждый бин который создается в контейнере Spring имеет имеет имя.
Что бы вывести имена бинов можно воспользоваться следующим кодом:

```java
 Arrays.stream(ctx.getBeanDefinitionNames()).forEach(beanName -> System.out.println(beanName));
```

С помощью данного кода получем из контейнера все имена бинов и выводим их на экран.

Имена формируются по следующим правилам

1. Автоматическая генерация имени бин
   
```java
@Component
public class UserService {
}
```

Имя бина будет совпадать с названием класса, но начинается с строчной буквы - userService

2. Указание имени бина вручную
   
```java
@Component("customUserService")
public class UserService {
}
```

Имя бина -  customBeanName

---

#### Правила автосвязывания

|Способ |Описание|
|-|-|
|@Autowired (по типу)|Внедрение зависимостей по типу класса|
|@Qualifier (по имени)|Позволяет указать конкретный бин при наличии нескольких|
|@Primary|Указывает бин как приоритетный при наличии нескольких кандидатов|
|@Resource(name = "...")|Внедрение зависимости по имени бина (из JSR-250)|
|@Inject|Альтернатива @Autowired из Java CDI (JSR-330)|

Рекомендуемый тип автосвязывания по типу через конструктор.

### 🟢  Жизненный цикл бинов

Жизненный цикл бина в Spring включает несколько этапов — от создания до уничтожения. Spring предоставляет механизмы для управления этим процессом, включая внедрение зависимостей, инициализацию и завершение.

Документация - <https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html>

### 🟢 Внедрение параметров

Документация - <https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/value-annotations.html>

---

#### Внедрение простых параметров

Для передачи простых параметров используется аннотация @Value

```java
@Component("providerValue")
public class ProviderValueImpl  implements Provider{

    @Value("Message from provider value")
    private String message;

    @Value("10")
    private int count;

    @Value("true")
    private boolean isSimple;

    @Override
    public String getMessage() {
        return String.format("Message -  %s, %d, %b", message, count,isSimple);
    }

}
```

#### Использование @PropertySource

В Spring @PropertySource используется для загрузки свойств из внешних файлов (например, .properties или .yml) в контекст приложения. Это полезно для хранения конфигурационных параметров отдельно от кода.

```java
@Component("providerResource")
@PropertySource("application.properties")
public class ProviderResource implements Provider {

    @Value("${filename}")
    private String fileName;

    @Override
    public String getMessage() {
        return  fileName;
    }
    
}
```

---

#### Внедрение параметров с использованием SpEL

SpEL (Spring Expression Language) — это мощный механизм выражений, который позволяет внедрять динамические значения в зависимости, параметры и свойства в Spring. С помощью SpEL можно выполнять вычисления, вызывать методы и работать с коллекциями прямо внутри конфигураций Spring.
Документация -  <https://docs.spring.io/spring-framework/reference/core/expressions.html>

Класс Property предоставляет значения.

```java
@Component("property")
public class Property {
    private final String version = "1.0.0";

    public String getVersion() {
        return version;
    }

    private final String name = "Component";

    public String getName() {
        return name;
    }

    private final String author = "Sergey";

    public String getAuthor() {
        return author;
    }

    private final List<String> numList = Arrays.asList("1", "2", "3", "4", "5");

    public List<String> getNumList() {
        return numList;
    }

    public String getFileName() {
        Properties prop = new Properties();
        try {
            prop.load(Property.class.getClassLoader().getResourceAsStream("application.properties"));
            return prop.getProperty("filename");

        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}

```

Класс ProviderSpELImpl внедряет использую механизм SpEL.

```java
@Component("providerSpEL")
public class ProviderSpELImpl  implements Provider{
    
    @Value("#{property.name}") // Внедрение параметра с помощью SpEL
    private String appName;

    @Value("#{property.version}")
    private String appVersion;

    @Value("#{property.name} - #{property.version} by #{property.author}") // Внедрение с использованием SpEL
    private String appFullInfo;

   @Value("#{T(String).join('---',property.numList)}")
    private String join;

    @Value("#{property.fileName}")
    private String filename;

    @Override
    public String getMessage() {
        return  "App Name: " + appName + "\n" +
                "App Version: " + appVersion + "\n" +
                "Full Info: " + appFullInfo + "\n" + 
                "File name from application.properties: " + filename + "\n" + 
                "Sum: " + join + "\n";
    }

}
```

### 🟢 Области видимости бинов(Scopes). Режимы получения бинов

В Spring область видимости (scope) определяет жизненный цикл и область действия бина. Spring поддерживает 5 основных и несколько кастомных скопов.

1. Singleton (по умолчанию). Один и тот же объект используется во всех зависимостях.
  
```java
@Component
public class SingletonBean {
}
```

2. Prototype. Новый экземпляр бина создаётся при каждом запросе

```java
@Component
@Scope("prototype")
public class PrototypeBean {
}
```

3. Request (только для Web-приложений). Один экземпляр бина на HTTP-запрос

```java
@Component
@Scope("request")
public class RequestBean {
}
```

4. Session (только для Web-приложений). Один экземпляр бина на HTTP-сессию

```java
@Component
@Scope("session")
public class SessionBean {
}
```

5. Application (только для Web-приложений). Один экземпляр бина на весь ServletContext (приложение)

```java
@Component
@Scope("application")
public class ApplicationBean {
}
```

#### Основные этапы жизненного цикла бина

1. Создание экземпляра бина
2. Внедрение зависимостей (DI - Dependency Injection)
3. Вызов методов @PostConstruct
4. Использование бина в приложении
5. Вызов методов @PreDestroy перед уничтожением
6. Удаление бина из контейнера

---

#### Создание экземпляра бина

Spring создаёт объект (экземпляр класса) и загружает его в контекст.

```java
@Component
public class MyBean {
    public MyBean() {
        System.out.println("1. Конструктор MyBean вызван");
    }
}
```

---

#### Внедрение зависимостей (DI)

Spring находит зависимости бина и автоматически внедряет их.

```java
@Component
public class Dependency {
    public Dependency() {
        System.out.println("Dependency создан");
    }
}

@Component
public class MyBean {
    private final Dependency dependency;

    @Autowired
    public MyBean(Dependency dependency) {
        this.dependency = dependency;
        System.out.println("2. Зависимость внедрена в MyBean");
    }
}
```

---

#### Вызов методов инициализации (@PostConstruct)

После внедрения зависимостей Spring вызывает методы инициализации.

```java
@Component
public class MyBean {
    @PostConstruct
    public void init() {
        System.out.println("3. Вызван @PostConstruct");
    }
}
```

---

#### Использование бина в приложении

На этом этапе объект готов к использованию.

```java
@Component
public class MyService {
    private final MyBean myBean;

    @Autowired
    public MyService(MyBean myBean) {
        this.myBean = myBean;
    }

    public void doWork() {
        System.out.println("4. Использование бина");
    }
}
```

---

#### Завершение работы и уничтожение бина (@PreDestroy)

Перед завершением работы контейнера Spring вызывает методы уничтожения бина.

```java
@Component
public class MyBean {
    @PreDestroy
    public void cleanup() {
        System.out.println("5. Вызван @PreDestroy");
    }
}
```

---

#### Пример полного жизненного цикла бина

```java
@Component
public class MyBean implements InitializingBean, DisposableBean {
    
    public MyBean() {
        System.out.println("1. Конструктор MyBean вызван");
    }

    @Autowired
    public void setDependency(Dependency dependency) {
        System.out.println("2. Внедрение зависимости");
    }

    @PostConstruct
    public void init() {
        System.out.println("3. Вызван @PostConstruct");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("3. Вызван afterPropertiesSet()");
    }

    public void use() {
        System.out.println("4. Использование бина");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("5. Вызван @PreDestroy");
    }

    @Override
    public void destroy() {
        System.out.println("5. Вызван destroy()");
    }
}
```

## 🔴 Литература

1. Spring 5 для профессионалов. Хо Кларенс, Козмина Юлиана, Шефер Крис, Харроп Роб, Диалектика-Вильямс, 2019 (Глава 3)
2. Pro Spring 6. An In-Depth Guide to the Spring Framework. Iuliana Cosmina, Rob Harrop, Chris Schaefer, and Clarence Ho. Apress Berkeley, CA 2023 (Глава 3)
3. <https://docs.spring.io/spring-framework/reference/core.html>
