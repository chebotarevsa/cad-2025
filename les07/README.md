# Управления объектами. Язык запросов JPQL

## Операции над объектами с использованием API Hibernate SessionFactory, Session

SessionFactory — это один из важнейших компонентов архитектуры Hibernate. Он представляет собой «фабрику» по созданию и управлению сессиями (Session), которые используются для взаимодействия приложения с базой данных. Каждый объект Session используется для выполнения CRUD-операций с базой данных, выполнения транзакций и запросов.

SessionFactory выполняет следующие задачи:

+ Создаёт и конфигурирует подключения к базе данных.
+ Управляет кэшированием объектов (кэш второго уровня и кэш метаданных).
+ Поддерживает пул соединений и настройки подключения.
+ Управляет загрузкой и конфигурацией сущностей (Entity) и маппингов.

Основные особенности SessionFactory:

+ Тяжёлый объект: Создание SessionFactory ресурсоёмко (занимает память и время), поэтому рекомендуется создавать его один раз при старте приложения и переиспользовать.
+ Потокобезопасный (Thread-safe): Может безопасно использоваться несколькими потоками одновременно.
+ Immutable (неизменяемый): Конфигурация SessionFactory после создания не изменяется.
+ Хранение метаданных: Содержит все маппинги сущностей, SQL-диалекты, информацию о базе данных.

SessionFactory создаётся на основе конфигурационного файла Hibernate, например, hibernate.cfg.xml:

``` java
SessionFactory sessionFactory = new Configuration()
    .configure("hibernate.cfg.xml")
    .buildSessionFactory();
```

+ Метод configure() указывает на файл конфигурации Hibernate.
+ Метод buildSessionFactory() создаёт и возвращает объект SessionFactory.

Конфигурационный файл

``` xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
  "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
  "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
  
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/mydb</property>
        <property name="hibernate.connection.username">postgres</property>
        <property name="hibernate.connection.password">12345</property>
        <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
        <property name="hibernate.hbm2ddl.auto">update</property>

        <!-- Список маппингов -->
        <mapping class="ru.bsuedu.cad.demo.entities.Student"/>
        <mapping class="ru.bsuedu.cad.demo.entities.Group"/>
        <mapping class="ru.bsuedu.cad.demo.entities.Course"/>
    </session-factory>
</hibernate-configuration>
```

или с использован java кода (см. [demo](./demo/app/src/main/java/ru/bsuedu/cad/demo/ConfigHibernate.java))

``` java
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

    @Bean(destroyMethod = "close")
    public LocalSessionFactoryBean sessionFactory() {
        var sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("ru.bsuedu.cad.demo.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
```

После создания SessionFactory, вы можете получить объекты Session:

``` java
    Session session = sessionFactory.openSession();
```

Session используется для выполнения операций с базой данных (создание, чтение, обновление, удаление).
Каждый раз, когда нужно выполнить операции с базой данных, открывается новая сессия.
Объект Session не потокобезопасный.

Рассмотрим пример на основе следующие модели данных

``` mermaid

erDiagram
    demo_group ||--o{ demo_student : "содержит"
    demo_group ||--o{ demo_group_course : "связана_с"
    demo_course ||--o{ demo_group_course : "связана_с"

    demo_group {
        int ID PK
        int NUMBER
        string DESCRIPTION
    }

    demo_student {
        int ID PK
        string NAME
        int GROUP_ID FK
    }

    demo_course {
        int ID PK
        string DESCRIPTION
    }

    demo_group_course {
        int GROUP_ID FK
        int COURSE_ID FK
    }
```

Рассмотрим реализацию паттерна DAO для сущности Student.
В каждом методе создается новый объект Session, выполняется операция и сессия закрывается.
Операция может выполняться транзакционно.

``` java
public class StudentDAO {

    private SessionFactory sessionFactory;

    public StudentDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Student student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        }
    }

    public Student findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Student.class, id);
        }
    }

    public void update(Student student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(student);
            tx.commit();
        }
    }

    public void delete(Student student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(student);
            tx.commit();
        }
    }

    public List<Student> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Student s WHERE s.name = :name", Student.class)
                          .setParameter("name", name)
                          .getResultList();
        }
    }
}
```

HQL (Hibernate Query Language) — это объектно-ориентированный язык запросов, который используется в Hibernate для работы с сущностями (объектами) напрямую, а не с таблицами базы данных.

+ Работает с сущностями и полями, а не таблицами и колонками.
+ Независим от конкретной базы данных.
+ Удобен для разработчиков на Java, так как приближен к объектной модели данных.

## Операции над объектами с использованием API JPA

EntityManager —  является аналогом сессии Hibernate (org.hibernate.Session), но это стандартизированный API (часть спецификации JPA), предназначенный для управления объектами-сущностями и взаимодействия с базой данных.

Предназначен для выполннения следующих операций:

+ Сохранение (persist)
+ Получение (find)
+ Обновление (merge)
+ Удаление (remove)
+ Выполнение запросов (JPQL и Criteria API)

Имеется два типа работы EntityManager:

+ Container-managed (рекомендуется):
  + Создание и закрытие EntityManager контролируется Spring.
  + Транзакции полностью управляются контейнером с помощью аннотации @Transactional.
+ Application-managed:
  + Разработчик явно контролирует весь жизненный цикл (создание, транзакции, закрытие).
  + Используется редко (например, в небольших приложениях или при написании утилитных методов).

| Этап                       | Container-managed EntityManager (Spring)                           | Application-managed EntityManager (ручное управление)          |
|----------------------------|--------------------------------------------------------------------|----------------------------------------------------------------|
| **Создание**               | Автоматически Spring-контейнером                                   | `entityManagerFactory.createEntityManager();`                  |
| **Начало транзакции**      | Автоматически при вызове метода с `@Transactional`                 | `entityManager.getTransaction().begin();`                      |
| **Получение**              | `@PersistenceContext private EntityManager em;`                    | `EntityManager em = entityManagerFactory.createEntityManager();`|
| **Commit транзакции**      | Автоматически при успешном завершении метода с `@Transactional`    | `entityManager.getTransaction().commit();`                     |
| **Rollback транзакции**    | Автоматически при исключении (`RuntimeException`)                  | `entityManager.getTransaction().rollback();`                   |
| **Закрытие EntityManager** | Автоматически контейнером (Spring)                                 | `entityManager.close();`                                       |

Жизненный цикл JPA-сущности описывает состояния объекта, которыми управляет EntityManager в процессе работы с JPA/Hibernate.

| Состояние сущности       | Описание состояния                               | Действия, приводящие к состоянию           |
|--------------------------|--------------------------------------------------|--------------------------------------------|
| **Transient (новый)**    | Сущность создана в Java-коде, но ещё не связана с persistence context и не сохранена в БД | `new Entity()`                             |
| **Managed (управляемый)**| Сущность находится под управлением EntityManager (привязана к persistence context) и связана с записью в БД | `entityManager.persist(entity)`<br>`entityManager.merge(entity)`<br>`entityManager.find(Class,id)` |
| **Detached (отсоединён)**| Сущность ранее была управляемой (managed), но сейчас не связана с текущим persistence context | Закрытие EntityManager<br>`entityManager.detach(entity)`<br>завершение транзакции или сессии |
| **Removed (удалён)**     | Сущность помечена для удаления                   | `entityManager.remove(entity)`             |

Изменение конфигурации для использования EntityManager
 
 ``` java
 @Import(ConfigBasic.class)
@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
@EnableTransactionManagement
public class ConfigJpa {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfigBasic.class);

    @Autowired
    DataSource dataSource;


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = 
            new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        
        em.setPackagesToScan("ru.bsuedu.cad.demo.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        em.setJpaVendorAdapter(vendorAdapter);

        // Дополнительные свойства JPA/Hibernate
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "create-drop");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.USE_SQL_COMMENTS, false);
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.MAX_FETCH_DEPTH, 3);
        properties.put(Environment.STATEMENT_BATCH_SIZE, 10);
        properties.put(Environment.STATEMENT_FETCH_SIZE, 50);
        em.setJpaProperties(properties);

        return em;
    }
    @Bean
    public PlatformTransactionManager transactionManager(
        @Autowired EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}

```

Изменение репозитория StudentDAO для использования EntityManager

``` java
@Repository("jpaStudentRepository")
public class StudentDAO implements StudentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Сохранение нового студента
    @Transactional
    @Override
    public void save(Student student) {
        entityManager.persist(student);
    }

    // Получение студента по id
    @Override
    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    // Обновление студента
    @Transactional
    @Override
    public void update(Student student) {
        entityManager.merge(student);
    }

    // Удаление студента
    @Transactional
    @Override
    public void delete(Student student) {
        entityManager.remove(
            entityManager.contains(student) ? student : entityManager.merge(student));
    }

    // Поиск студентов по имени с использованием JPQL
    @Override
    public List<Student> findByName(String name) {
        return entityManager.createQuery("FROM Student s WHERE s.name = :name", Student.class)
                            .setParameter("name", name)
                            .getResultList();
    }

}

```

## JPQL

JPQL (Java Persistence Query Language) — это язык объектно-ориентированных запросов, часть стандарта Java Persistence API (JPA). Он позволяет выполнять запросы не к таблицам, а напрямую к сущностям и их атрибутам, что делает запросы независимыми от используемой базы данных.


JPQL (Java Persistence Query Language) — это объектно-ориентированный язык запросов, который:

+ Очень похож на SQL, но оперирует сущностями и атрибутами.
+ Независим от конкретной реализации базы данных.
+ Часть стандарта JPA, поэтому совместим с разными реализациями (Hibernate, EclipseLink и др.).


| JPQL                                     | SQL                                    |
|------------------------------------------|----------------------------------------|
| Работает с сущностями и их полями        | Работает с таблицами и их столбцами     |
| Использует Java-имена сущностей и атрибутов| Использует имена таблиц и колонок базы |
| Независим от диалекта конкретной базы данных| Зависим от конкретного диалекта SQL    |
| Чувствителен к регистру имён классов и полей| Обычно не чувствителен к регистру     |

---

### **Примеры JPQL запросов:**

Рассмотрим на примере сущности:

```java
@Entity
class Student {
    @Id
    private Long id;
    private String name;

    @ManyToOne
    private Group group;
}
```

### **Простой SELECT-запрос (выбрать всех студентов)**

```java
String jpql = "SELECT s FROM Student s";
List<Student> students = entityManager.createQuery(jpql, Student.class)
                                     .getResultList();
```

### **Запрос с условием WHERE**

```java
String jpql = "SELECT s FROM Student s WHERE s.name = :name";
List<Student> students = entityManager.createQuery(jpql, Student.class)
                                     .setParameter("name", "Иванов Иван")
                                     .getResultList();
```

### **Запрос с JOIN (связь между сущностями)**

```java
String jpql = "SELECT s FROM Student s JOIN s.group g WHERE g.number = :number";
List<Student> students = entityManager.createQuery(jpql, Student.class)
                                     .setParameter("number", 12002308)
                                     .getResultList();
```

---

### **Типы запросов JPQL:**

JPQL поддерживает различные операции:

- **SELECT** — выборка данных.
- **UPDATE** — обновление данных.
- **DELETE** — удаление данных.

**Пример UPDATE-запроса:**

```java
entityManager.getTransaction().begin();

entityManager.createQuery("UPDATE Student s SET s.name = :newName WHERE s.name = :oldName")
             .setParameter("newName", "Петров Пётр")
             .setParameter("name", "Иванов Иван")
             .executeUpdate();

entityManager.getTransaction().commit();
```

---

### **Параметры в JPQL**

JPQL поддерживает два типа параметров:

- **Именованные параметры (рекомендуется):**  

  ```java
  query.setParameter("name", value);
  ```

- **Позиционные параметры (не рекомендуется, начиная с JPA 2.0 устарели):**  
  ```java
  query.setParameter(1, value);
  ```

---

### **Функции агрегирования и операторы**

JPQL поддерживает множество функций, таких как:  
- `COUNT()`
- `SUM()`
- `AVG()`
- `MIN/MAX`

**Пример:**

```java
Long count = entityManager.createQuery("SELECT COUNT(s) FROM Student s", Long.class)
                           .getSingleResult();
```

---

### **Основные отличия JPQL от HQL**

JPQL очень похож на HQL, так как последний является расширением JPQL. Основное отличие:

- **JPQL** — стандартный язык запросов (часть JPA).
- **HQL** — специфичный для Hibernate, расширяет JPQL дополнительными возможностями, но снижает переносимость.

Для универсальности (совместимость между JPA-реализациями) лучше использовать JPQL.
