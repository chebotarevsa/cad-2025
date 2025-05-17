# Введение в Spring Boot

## Проблемы разработки на "чистом" (Classic) Spring

### Типичный стек “чистого” Spring-приложения

Конфигурация приложения

+ Использование XML-файлов (например, applicationContext.xml, dispatcher-servlet.xml)
+ Или Java-конфигурации (@Configuration, @Bean, @ComponentScan)
+ Часто оба способа используются вместе

Настройка сервера

+ Добавление зависимостей вручную: Spring Core, Spring Web, Jackson, Hibernate и др.
+ Создание web.xml:
+ Регистрация DispatcherServlet
+ Настройка слушателей (ContextLoaderListener)
+ WAR-архивация и деплой на внешний Tomcat

Запуск

+ Открытие Tomcat Manager
+ Деплой WAR-файла
+ Ошибки конфигурации → стек трейсы и долгое устранение

### Проблемы

|Проблема|Пример|
|--|--|
|Сложная настройка|Требуется много boilerplate-конфигураций|
|Много ручной работы|Нужно вручную подключать зависимости и настраивать бин-фабрики|
|Сложность тестирования|Трудно “поднять” приложение без контейнера|
| Медленный запуск|Зависимость от внешнего Tomcat, настройка WAR|
|Много вариантов конфигурации|XML, Java, аннотации — сложно выбрать и поддерживать единообразие|

### Чего бы хотелось

+ Минимум конфигурации — максимум результата.
+ Запуск приложения как обычного JAR, без сервера.
+ Легко добавлять нужные модули (Web, Security, JPA).
+ Единый стиль конфигурации.
+ Простота деплоя и мониторинга.

## Что такое Spring Boot?

Spring Boot — это инструмент, расширяющий Spring Framework, который упрощает создание, настройку и запуск приложений благодаря автоконфигурации, встроенному серверу и системе зависимостей (starter’ам).

Цель Spring Boot

+ Сократить время настройки.
+ Избавить от необходимости писать “болезненные” конфигурации.
+ Упростить запуск и деплой приложений.
+ Сделать Spring удобным даже для новичков.

### Автоконфигурация

+ Аннотация @EnableAutoConfiguration (включается внутри @SpringBootApplication)
+ Spring сам “угадывает”, какие бины нужно создать, основываясь на зависимостях в classpath
  
Например, если есть spring-boot-starter-web, будет настроен:

+ Tomcat
+ DispatcherServlet
+ Jackson
+ RequestMappingHandlerMapping

### Стартер-зависимости (Starters)

Преднастроенные группы зависимостей
Примеры:

+ spring-boot-starter-web
+ spring-boot-starter-data-jpa
+ spring-boot-starter-security
  
+ Вам не нужно указывать каждую зависимость вручную (например, для Web — Spring MVC + Tomcat + Jackson → всё в одном стартере)

### Встроенный сервер (Embedded Server)

+ Встроенный Tomcat (по умолчанию), Jetty или Undertow
+ Нет нужды устанавливать внешний сервер
+ Приложение — это JAR, который можно запустить как обычный .exe:

``` bash
java -jar demo-0.0.1-SNAPSHOT.jar
```

### Единый файл конфигурации

+ application.properties или application.yml
+ Хранение настроек в централизованном виде
+ Поддержка профилей (application-dev.properties, application-prod.properties)

### Аннотация @SpringBootApplication

Комбинирует:

+ @Configuration
+ @ComponentScan
+ @EnableAutoConfiguration
+ Делает стартовую точку приложения компактной и читаемой:


``` java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

## Архитектура Spring Boot и основные аннотации

+ Создаётся контекст Spring (ApplicationContext)
+ Запускается механизм автоконфигурации
+ Производится сканирование компонентов
+ Стартует встроенный Tomcat


### Автосонфигурирование

+ В Spring Boot есть модуль: spring-boot-autoconfigure
+ Он содержит множество конфигураций, отмеченных аннотацией @Conditional...
  Пример: WebMvcAutoConfiguration активируется, если:
  + В classpath есть Spring MVC
  + Нет вашей собственной конфигурации WebMvcConfigurer

### @Conditional

Чтобы обеспечить большую гибкость условной регистрации bean-компонентов Spring, Spring 4 ввел концепцию @Conditional. Используя подход @Conditional, вы можете зарегистрировать компонент, условно основанный на любом произвольном условии.

Например, вы можете зарегистрировать компонент, когда:

+ Определенный класс присутствует в classpath
+ Spring bean определенного типа еще не зарегистрирован в ApplicationContext
+ Определенный файл существует в местоположении
+ Конкретное значение свойства настраивается в файле конфигурации
+ Определенное системное свойство присутствует / отсутствует

``` java
@Configuration
public class AppConfig
{
    @Bean
    @Conditional(MySQLDatabaseTypeCondition.class)
    public UserDAO jdbcUserDAO(){
        return new JdbcUserDAO();
    }

    @Bean
    @Conditional(MongoDBDatabaseTypeCondition.class)
    public UserDAO mongoUserDAO(){
        return new MongoUserDAO();
    }
}
```

``` java
public class MongoDBDatabaseTypeCondition implements Condition
{
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata)
    {
        String enabledDBType = System.getProperty("dbType");
        return (enabledDBType != null && enabledDBType.equalsIgnoreCase("MONGODB"));
    }
}
```

### @EnableAutoConfiguration

Аннотация @EnableAutoConfiguration включает автоматическую настройку Spring ApplicationContext путем сканирования компонентов пути к классам и регистрации бинов, соответствующих различным условиям.

Spring Boot предоставляет различные классы AutoConfiguration в spring-boot-autoconfigure-{version}.jar, которые отвечают за регистрацию различных компонентов.

Обычно классы AutoConfiguration аннотируются @Configuration, чтобы пометить его как класс конфигурации Spring, и аннотируются @EnableConfigurationProperties для привязки свойств настройки и одного или нескольких методов регистрации условного компонента.

``` java
@Configuration
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ Registrar.class, DataSourcePoolMetadataProvidersConfiguration.class })
public class DataSourceAutoConfiguration 
{
    ...
    ...
    @Conditional(DataSourceAutoConfiguration.EmbeddedDataSourceCondition.class)
    @ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
    @Import(EmbeddedDataSourceConfiguration.class)
    protected static class EmbeddedConfiguration {

    }

    @Configuration
    @ConditionalOnMissingBean(DataSourceInitializer.class)
    protected static class DataSourceInitializerConfiguration {
        @Bean
        public DataSourceInitializer dataSourceInitializer() {
        return new DataSourceInitializer();
        }
    }

    @Conditional(DataSourceAutoConfiguration.NonEmbeddedDataSourceCondition.class)
    @ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
    protected static class NonEmbeddedConfiguration {
        @Autowired
        private DataSourceProperties properties;

        @Bean
        @ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
        public DataSource dataSource() {
            DataSourceBuilder factory = DataSourceBuilder
                    .create(this.properties.getClassLoader())
                    .driverClassName(this.properties.getDriverClassName())
                    .url(this.properties.getUrl()).username(this.properties.getUsername())
                    .password(this.properties.getPassword());
            if (this.properties.getType() != null) {
                factory.type(this.properties.getType());
            }
            return factory.build();
        }
    }
    ...
    ...
    @Configuration
    @ConditionalOnProperty(prefix = "spring.datasource", name = "jmx-enabled")
    @ConditionalOnClass(name = "org.apache.tomcat.jdbc.pool.DataSourceProxy")
    @Conditional(DataSourceAutoConfiguration.DataSourceAvailableCondition.class)
    @ConditionalOnMissingBean(name = "dataSourceMBean")
    protected static class TomcatDataSourceJmxConfiguration {
        @Bean
        public Object dataSourceMBean(DataSource dataSource) {
        ....
        ....
        }
    }
    ...
    ...
}
```

Здесь DataSourceAutoConfiguration аннотируется @ConditionalOnClass({DataSource.class,EmbeddedDatabaseType.class}), что означает, что автоконфигурация bean-компонентов в DataSourceAutoConfiguration будет рассматриваться, только если классы DataSource.class и EmbeddedDatabaseType.class доступны в classpath.

Класс также аннотируется @EnableConfigurationProperties(DataSourceProperties.class), который позволяет автоматически связывать свойства в application.properties со свойствами класса DataSourceProperties.

## Spring Initializr

Spring Initializr (<https://start.spring.io/>) — это официальный веб-сервис, который позволяет быстро сгенерировать заготовку Spring Boot-приложения с нужными зависимостями, структурой и настройками.

