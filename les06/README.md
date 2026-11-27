# ORM. Применение Hibernate в Spring

**ORM** (англ. Object-Relational Mapping, рус. объектно-реляционное отображение, или преобразование) — технология программирования, которая связывает базы данных с концепциями объектно-ориентированных языков программирования, создавая «виртуальную объектную базу данных»

**Hibernate** — это популярный фреймворк для объектно-реляционного отображения (ORM), который позволяет Java-разработчикам работать с базами данных с использованием объектно-ориентированного подхода, минимизируя необходимость написания SQL-запросов. Hibernate автоматизирует процесс преобразования объектов Java в строки базы данных и наоборот, обеспечивая удобный и эффективный механизм для взаимодействия с реляционными базами данных.

## ORM

Концепция ORM (Object-Relational Mapping)

ORM (Object-Relational Mapping) — это технология, которая позволяет разработчикам работать с базами данных, используя объектно-ориентированные принципы программирования. В контексте ORM объектно-ориентированная модель данных (сущности, объекты) отображается на реляционную модель данных (таблицы, строки). Это позволяет программам работать с базой данных, используя объекты и их свойства, вместо того чтобы писать и исполнять SQL-запросы вручную.

### Объектно-реляционное расхождение (Impedance Mismatch)

| Особенность          | Объектно-ориентированное программирование (ООП) | Реляционные базы данных (РБД) | Решение в ORM-системах |
|----------------------|------------------------------------------------|------------------------------|-------------------------|
| **Структура данных** | Объекты с полями и методами                   | Таблицы с колонками         | ORM сопоставляет классы с таблицами (Entity Mapping) |
| **Связи между сущностями** | Ассоциации (композиция, агрегация, наследование) | Внешние ключи, JOIN-запросы | ORM поддерживает аннотации (`@OneToMany`, `@ManyToMany`), ленивую/жадную загрузку |
| **Идентификация**    | Объекты идентифицируются по ссылке (указателю) | Записи идентифицируются первичным ключом (ID) | ORM автоматически генерирует ID или использует стратегию (`GenerationType.AUTO`) |
| **Связанность**      | Поддержка сложных иерархий объектов           | Плоская структура, связи через внешние ключи | ORM управляет связями с каскадным сохранением (`CascadeType.ALL`) |
| **Изменяемость**     | Объекты могут изменяться в памяти             | Записи требуют явного обновления в БД | ORM автоматически отслеживает изменения объектов и применяет их в БД (`@Transactional`) |
| **Обход структуры**  | Объекты могут ссылаться друг на друга через указатели | Данные соединяются через SQL-запросы (JOIN) | ORM реализует ленивую (`Lazy`) и жадную (`Eager`) загрузку данных |
| **Типизация данных** | Поддержка сложных типов (коллекции, вложенные объекты) | Только примитивные типы (int, varchar, date) | ORM преобразует объекты в примитивные типы (например, `@Embeddable` в Hibernate) |
| **Полиморфизм**      | Есть встроенный механизм полиморфизма         | Отсутствует, требует специальных схем (Single Table Inheritance, Table per Class) | ORM поддерживает стратегии наследования (`@Inheritance(strategy = InheritanceType.JOINED)`) |
| **Производительность** | Операции выполняются в памяти, доступ мгновенный | Запросы требуют обращений к диску, возможны задержки | ORM кэширует данные (`@Cacheable`), использует `FetchType.LAZY` |
| **Транзакции**       | Объекты изменяются в памяти, без необходимости фиксации | Требуется явное управление транзакциями (commit, rollback) | ORM использует автоматическое управление транзакциями (`@Transactional`) |

ORM помогает решить проблему “объектно-реляционного расхождения” (Object-Relational Impedance Mismatch), которая возникает из-за различий в моделях данных между объектно-ориентированным программированием и реляционными базами данных. Основная цель ORM — упростить взаимодействие между объектами в коде и данными, хранящимися в базе данных.

### Основные принципы ORM

1. Объекты и таблицы: В ORM система объектов Java (или другого языка) отображается на таблицы базы данных. Каждый объект обычно соответствует одной строке таблицы.
2. Атрибуты объектов и столбцы: Свойства объектов отображаются на столбцы таблицы. Атрибуты объекта (например, поля класса) являются атрибутами, которые будут храниться в базе данных.
3. Связи между объектами и внешние ключи: Ассоциации между объектами (например, отношения “один к одному”, “один ко многим”, “многие ко многим”) отображаются на внешние ключи в базе данных.
4. SQL-запросы через объекты: Вместо написания SQL-запросов вручную, разработчик может работать с объектами, а ORM фреймворк сам генерирует SQL-запросы для выполнения операций с базой данных.

### Преимущества ORM

1. Абстракция SQL-запросов: ORM фреймворк автоматически генерирует SQL-запросы для стандартных операций, таких как создание, чтение, обновление и удаление данных (CRUD), избавляя разработчика от необходимости писать SQL вручную.
2. Упрощение работы с базой данных: ORM позволяет работать с данными как с объектами, что соответствует принципам объектно-ориентированного программирования. Это снижает сложность взаимодействия с базой данных и позволяет разработчику сосредоточиться на бизнес-логике.
3. Переносимость: Приложение, использующее ORM, становится менее зависимым от конкретной СУБД (системы управления базами данных). При изменении базы данных (например, переход с MySQL на PostgreSQL) требуется минимальные изменения в коде приложения.
4. Управление транзакциями: ORM-фреймворки часто включают в себя механизмы управления транзакциями, что помогает гарантировать атомарность операций с базой данных и упрощает работу с транзакциями.
5. Поддержка кэширования: Многие ORM системы включают в себя механизмы кэширования на уровне сессии или второго уровня, что позволяет улучшить производительность за счет сокращения количества запросов к базе данных.
6. Управление связями между сущностями: ORM упрощает управление связями между сущностями, такими как связи “один к одному”, “один ко многим” и “многие ко многим”.

### Проблемы и ограничения ORM

1. Производительность: В некоторых случаях использование ORM может привести к ухудшению производительности по сравнению с ручной оптимизацией SQL-запросов. Например, чрезмерное количество запросов, не оптимизированные запросы или ошибки в конфигурации могут вызвать избыточную нагрузку на базу данных.
2. Сложность в сложных запросах: ORM хорошо подходит для работы с базовыми CRUD-операциями, но в случае сложных запросов, которые требуют нескольких объединений таблиц или сложных условий, может возникнуть необходимость в ручной оптимизации запросов.
3. Миграции и версии базы данных: ORM не всегда может эффективно управлять миграциями схемы базы данных при изменении бизнес-логики, что требует дополнительных инструментов или библиотек.
4. Объектно-реляционное расхождение: Несмотря на то что ORM решает многие проблемы объектно-реляционного расхождения, иногда между объектно-ориентированными моделями и реляционными схемами все равно возникают трудности, такие как различия в типах данных, связи и особенности производительности.

### Как работает ORM: Пример

Предположим, у нас есть классы Persona и Character, которые мы хотим отобразить на соответствующие таблицы в базе данных.

1. Модели данных (сущности):

``` java
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "personas")
public class Persona {

    @Id
    private Long id;
    private String name;
    private int level;

    @ManyToOne
    private Character character;

    // геттеры и сеттеры
}

@Entity
@Table(name = "characters")
public class Character {

    @Id
    private Long id;
    private String name;

    // геттеры и сеттеры
}

```

В этом примере мы определяем две сущности: Persona и Character. Связь между ними установлена с помощью аннотации @ManyToOne, которая означает, что одна персона может принадлежать одному персонажу.

2. Хранение данных:

Когда мы сохраняем объект Persona, ORM фреймворк автоматически генерирует SQL-запрос, чтобы вставить строку в таблицу personas. Также он автоматически создает ссылку на строку в таблице characters через внешний ключ.

``` java
Persona persona = new Persona();
persona.setName("John Doe");
persona.setLevel(50);
persona.setCharacter(character); // character — это объект класса Character

entityManager.persist(persona);  // Сохраняем объект в базе данных
```

В результате вызова метода persist(), ORM автоматически сгенерирует SQL-запросы, чтобы вставить данные в таблицы, и выполнит их.
3. Извлечение данных:
   
``` java
Persona retrievedPersona = entityManager.find(Persona.class, 1L); // Находим персону по ID
System.out.println(retrievedPersona.getName()); // Выводим имя персоны
``` 

ORM будет автоматически преобразовывать результаты SQL-запроса в объекты Java, избавляя разработчика от необходимости вручную извлекать данные из базы и преобразовывать их в объекты.

Популярные ORM-фреймворки

1. Hibernate — один из самых популярных фреймворков ORM для Java. Это мощный инструмент, который поддерживает широкие возможности для работы с базами данных и отлично интегрируется с Spring.
2. MyBatis — это фреймворк, который тоже позволяет работать с базой данных, но в отличие от традиционного ORM, он больше ориентирован на использование SQL-запросов и их маппинг на объекты, чем на полную абстракцию базы данных. Это не совсем ORM система, это Query Mapper (или Data Mapper) система.
3. EclipseLink — это еще один популярный фреймворк для работы с JPA, предлагающий мощные возможности для работы с базами данных.

## Спецификация JPA и ее реализация в библиотеке Hibernate

**Java Persistence API (JPA)** — это спецификация для работы с объектно-реляционными данными в Java-приложениях. Она определяет стандартизированные интерфейсы для маппинга объектов Java на реляционные базы данных, но не является конкретной реализацией.

**Hibernate** — это одна из самых популярных реализаций JPA. Он предоставляет мощный ORM (Object-Relational Mapping) механизм, позволяя разработчикам работать с базой данных через объектно-ориентированный подход.

### Подключение Hibernate к  проекту  Spring.

``` java
@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
public class ConfigHibernate {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfigBasic.class);

    @Autowired
    DataSource dataSource;

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.put(Environment.HBM2DDL_AUTO, "create-drop");
        hibernateProp.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        hibernateProp.put(Environment.FORMAT_SQL, true);
        hibernateProp.put(Environment.USE_SQL_COMMENTS, false);
        hibernateProp.put(Environment.SHOW_SQL, true);
        hibernateProp.put(Environment.MAX_FETCH_DEPTH, 3);
        hibernateProp.put(Environment.STATEMENT_BATCH_SIZE, 10);
        hibernateProp.put(Environment.STATEMENT_FETCH_SIZE, 50);
        return hibernateProp;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        var sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("ru.bsuedu.cad.demo.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
}

```


| Свойство | Значение (пример) | Описание |
|----------|------------------|----------|
| `hibernate.dialect` | `org.hibernate.dialect.PostgreSQLDialect` | Указывает диалект базы данных. |
| `hibernate.show_sql` | `true` | Показывает SQL-запросы в консоли. |
| `hibernate.format_sql` | `true` | Форматирует SQL-запросы для удобного чтения. |
| `hibernate.hbm2ddl.auto` | `update` | Стратегия управления схемой БД (`none`, `update`, `create`, `create-drop`). |
| `hibernate.connection.driver_class` | `org.postgresql.Driver` | JDBC-драйвер для базы данных. |
| `hibernate.connection.url` | `jdbc:postgresql://localhost:5432/mydb` | URL подключения к базе данных. |
| `hibernate.connection.username` | `myuser` | Имя пользователя для подключения к БД. |
| `hibernate.connection.password` | `mypassword` | Пароль пользователя БД. |
| `hibernate.connection.pool_size` | `10` | Размер пула соединений. |
| `hibernate.jdbc.batch_size` | `50` | Количество записей в батч-операции. |
| `hibernate.cache.use_second_level_cache` | `true` | Включает второй уровень кэша. |
| `hibernate.cache.use_query_cache` | `true` | Включает кэширование запросов. |
| `hibernate.generate_statistics` | `true` | Включает сбор статистики выполнения Hibernate. |
| `hibernate.order_inserts` | `true` | Оптимизирует вставку данных пакетами. |
| `hibernate.order_updates` | `true` | Оптимизирует обновление данных пакетами. |
| `hibernate.connection.autocommit` | `false` | Управляет автоматической фиксацией транзакций. |
| `hibernate.default_schema` | `public` | Устанавливает схему базы данных по умолчанию. |

### Концепции JPA

| Концепция               | Описание |
|-------------------------|----------|
| **Entity**             | Класс, который представляет таблицу в БД. |
| **Relationships**      | Определение связей между сущностями (`@OneToOne`, `@OneToMany`, `@ManyToMany`). |
| **Inheritance Mapping** | Поддержка стратегий наследования (`Single Table`, `Joined`, `Table per Class`). |
| **EntityManager**      | Основной интерфейс для работы с сущностями (CRUD-операции). |
| **Persistence Context** | Контекст управления объектами, аналогичный Unit of Work. |
| **JPQL (Java Persistence Query Language)** | Объектно-ориентированный язык запросов, аналог SQL. |
| **Transactions**       | Управление транзакциями (`commit`, `rollback`). |

#### Entity

В JPA сущность (Entity) — это класс, который представляет таблицу в базе данных.
Чтобы определить Entity, нужно использовать аннотацию @Entity, а также указать первичный ключ с помощью @Id.

```java
@Entity
@Table(name = "demo_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME", unique = false, nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;
}
```

1. @Entity — отмечает класс как сущность JPA.
2. @Table(name = "users") — указывает, что сущность соответствует таблице users (необязательно, если имя совпадает).
3. @Id — определяет первичный ключ.
4. @GeneratedValue(strategy = GenerationType.IDENTITY) — задает автоинкремент.
5. @Column — используется для настройки колонок (уникальность, nullable, длина).

Каждая сущность должна иметь @Id, который определяет первичный ключ.
Можно выбрать стратегию генерации:

|Стратегия|Описание|
|--|--|
|AUTO| Hibernate сам выбирает стратегию (по умолчанию).|
|IDENTITY| Использует автоинкремент (SERIAL в PostgreSQL, AUTO_INCREMENT в MySQL).|
|SEQUENCE| Использует SQL-секвенции (@SequenceGenerator).|
|TABLE|Хранит значения ключей в специальной таблице.|

``` java
    @Id
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    private Long id;
```

#### Relationships

##### Аннотация @OneToMany в JPA/Hibernate

Аннотация @OneToMany в JPA используется для установления связи “один ко многим” между сущностями. Это означает, что одна запись в одной таблице может быть связана с несколькими записями в другой таблице.

``` mermaid
erDiagram
    STUDENT {
        long id PK
        string name
    }
    
    GROUP {
        long id PK
        int NUMBER
        string DESCRIPTION
    }
    
    GROUP ||--o{ STUDENT : "has"
```

``` java
@Entity
@Table(name = "demo_group", indexes = {
    @Index(name = "idx_group_number", columnList = "number"),
})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NUMBER", unique = true, nullable = false)
    private int number;

    @Column(name="DESCRIPTION", unique = false, nullable = false, length = 100)
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();
}
```

``` java

@Entity
@Table(name = "demo_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME", unique = false, nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;
}
```

+ В одной  Group может может учиться много Student.
+ В классе Student используется @ManyToOne с внешним ключом (GROUP_ID).
+ В классе Group используется @OneToMany, указывая, что список students связан с group.
+ Параметр mappedBy = "group" указывает, что связь уже определена в Student.

Параметры аннотации @OneToMany

| Параметр         | Тип          | Описание |
|------------------|-------------|----------|
| `mappedBy`      | `String`     | Определяет поле в дочерней сущности, которое ссылается на родительскую. Используется для **двунаправленной связи**. |
| `cascade`       | `CascadeType[]` | Определяет, какие операции с родительской сущностью должны автоматически применяться к дочерним (`ALL`, `PERSIST`, `MERGE`, `REMOVE`, `REFRESH`, `DETACH`). |
| `fetch`         | `FetchType`  | Определяет стратегию загрузки связанных объектов: `LAZY` (лениво, по умолчанию) или `EAGER` (жадно). |
| `orphanRemoval` | `boolean`    | Если `true`, то удаляет дочерние сущности из БД при их удалении из коллекции в родительской сущности. |

##### Аннотация @ManyToMany в JPA/Hibernate

Аннотация @ManyToMany используется для создания много-ко-многим отношений между сущностями в базе данных. Это означает, что одна сущность может быть связана с несколькими записями другой сущности, и наоборот.

В реляционной базе данных связь “многие ко многим” реализуется через промежуточную таблицу, содержащую два внешних ключа.
В JPA связь можно реализовать двумя способами:

+ Без явной промежуточной таблицы (Hibernate создаст её автоматически).
+ С явной промежуточной таблицей (используется @JoinTable).

``` mermaid
erDiagram
    STUDENT {
        long student_id PK
        string name
    }
    
    COURSE {
        long course_id PK
        string name
    }
    
    STUDENT_COURSE {
        long student_id FK
        long course_id FK
    }
    
    STUDENT ||--o{ STUDENT_COURSE : enrolled_in
    COURSE ||--o{ STUDENT_COURSE : includes
```

```java
@Entity
@Table(name = "demo_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME", unique = false, nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToMany
    private Set<Course> courses = new HashSet<>();
}
```

```java
@Entity
@Table(name = "demo_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME", unique = false, nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToMany
    private Set<Course> courses = new HashSet<>();
}
```

@ManyToMany с явной промежуточной таблицей (@JoinTable)

``` java
     @ManyToMany
     @JoinTable(
         name = "demo_student_course",
         joinColumns = @JoinColumn(name = "student_id"),
         inverseJoinColumns = @JoinColumn(name = "course_id")
     )
     private Set<Course> courses = new HashSet<>();
```

+ В @JoinTable мы явно указали имя таблицы (student_course).
+ joinColumns указывает внешний ключ для Student.
+ inverseJoinColumns указывает внешний ключ для Course.

#### @MappedSuperclass в JPA/Hibernate

Аннотация @MappedSuperclass используется в JPA для указания, что класс является родительским, но не является сущностью (@Entity). Это позволяет избежать создания отдельной таблицы для него, но его поля будут унаследованы в дочерних сущностях.

Используется в случаях:

+ когда нужно избежать дублирования кода в нескольких сущностях.
+ когда не нужна отдельная таблица в базе данных для родительского класса.
+ когда нужно хранить общие атрибуты (id, created_at, updated_at) в одном месте.

```java
@MappedSuperclass
public class AbstractEntity {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
}

```

``` java
@Entity
@Table(name = "demo_course")
public class Course extends AbstractEntity{

    @Column(name="DESCRIPTION", unique = false, nullable = false, length = 100)
    private String description;


    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}

```

#### Inheritance Mapping в JPA/Hibernate

В объектно-ориентированном программировании наследование - это ключевой механизм повторного использования кода. Однако в реляционных базах данных отсутствует встроенная поддержка наследования. **JPA (Java Persistence API)** предоставляет механизм **Inheritance Mapping**, который позволяет маппировать иерархию классов на реляционные таблицы. Hibernate, как реализация JPA, поддерживает несколько стратегий наследования.


##### Основные стратегии наследования в JPA**

JPA предлагает три основных стратегии отображения наследования в БД:

| Стратегия | Описание | Подходит для |
|-----------|----------|--------------|
| **Single Table (Одна таблица на всю иерархию)** | Все дочерние классы хранятся в одной таблице с дополнительным столбцом `discriminator`. | Высокая производительность, простота структуры, но может привести к разреженным данным. |
| **Joined Table (Таблица на каждый класс)** | Каждая сущность имеет свою таблицу, и связи между ними устанавливаются через `JOIN`. | Гибкость, нормализация данных, но более сложные SQL-запросы. |
| **Table per Class (Таблица на каждый класс)** | Каждый класс создаёт свою таблицу без использования `JOIN`. | Высокая скорость чтения, но возможны дубликаты данных. |

---

##### Single Table (Одна таблица на всю иерархию)

```java
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

@Entity
@DiscriminatorValue("Student")
public class Student extends User {
    private String major;
}

@Entity
@DiscriminatorValue("Teacher")
public class Teacher extends User {
    private String subject;
}
```

+ Все сущности Student и Teacher хранятся в одной таблице user.
+ Hibernate добавляет столбец type, который указывает тип (Student или Teacher).
  
**Плюсы**: Простота структуры, высокая производительность.

**Минусы**: Пустые (NULL) значения в таблице для несвойственных полей.

#####  Joined Table (Таблица на каждый класс)

``` java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

@Entity
public class Student extends User {
    private String major;
}

@Entity
public class Teacher extends User {
    private String subject;
}
```

+ User создаётся как отдельная таблица.
+ Student и Teacher хранятся в отдельных таблицах с внешним ключом на User.
  
**Плюсы**: Нормализованная структура, нет дублирования данных.

**Минусы**: JOIN-запросы усложняют выборку данных.

##### Table per Class (Таблица на каждый класс)

``` java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

@Entity
public class Student extends User {
    private String major;
}

@Entity
public class Teacher extends User {
    private String subject;
}
```

+ Каждый дочерний класс (Student, Teacher) создаёт свою таблицу.
+ В User нет общей таблицы.


**Плюсы**: Высокая скорость поиска.

**Минусы**: Дублирование общих полей (например, name хранится во всех таблицах).


Выбор стратегии:

+ Single Table → Подходит для простых иерархий, где нет большого количества уникальных полей.
+ Joined Table → Хорошо подходит для гибких систем, где важна нормализация.
+ Table per Class → Уместно для производительности чтения, но с дублированием данных.
