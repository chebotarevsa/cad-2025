# Spring Data.Управление транзакциями

Spring Data — это проект из семейства Spring, который упрощает и стандартизирует работу с различными технологиями хранения данных (реляционными и NoSQL-базами данных).

Spring Data предоставляет абстракцию поверх различных технологий (JPA, JDBC, MongoDB, Redis и др.), избавляя разработчика от написания повторяющегося (boilerplate) кода.

Основные возможности Spring Data:

+ Создание репозиториев с минимальным количеством кода.
+ Автоматическая генерация стандартных CRUD-операций.
+ Поддержка создания запросов через именование методов.
+ Простое создание собственных и сложных запросов с помощью аннотации @Query.
+ Поддержка пагинации и сортировки.

``` kotlin

dependencies {
    // Spring Core и Context
    implementation("org.springframework:spring-context:6.1.5")

    // Spring Data JPA и ORM
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.springframework:spring-orm:6.1.5")

    // Hibernate (JPA provider)
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    // H2 Database
    implementation("com.h2database:h2:2.2.224")

    // Jakarta Persistence API
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

```

Аннотация:

``` java
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.demo.repository")
```

+ Включает поддержку репозиториев Spring Data JPA в приложении.
+ Указывает Spring, в каком пакете нужно искать интерфейсы репозиториев, наследующие интерфейсы из Spring Data (например, CrudRepository, JpaRepository).
+ Spring автоматически создаёт реализации этих интерфейсов во время старта приложения

## CrudRepository

CrudRepository — это основной интерфейс из проекта Spring Data, предоставляющий базовый набор CRUD-операций (создание, чтение, обновление и удаление) для работы с сущностями без необходимости писать дополнительный код.

+ Интерфейс CrudRepository входит в Spring Data и предоставляет набор стандартных методов CRUD (Create, Read, Update, Delete).
+ Работает с JPA, Hibernate, MongoDB, Redis и другими хранилищами через расширения Spring Data.
+ Автоматически генерирует реализацию методов на основе анализа объявленных интерфейсов репозиториев.


| Метод                                            | Описание                                                      | Пример использования                           |
|--------------------------------------------------|---------------------------------------------------------------|-----------------------------------------------|
| `save(entity)`                                   | Сохраняет новую или изменяет существующую сущность            | `studentRepository.save(student);`            |
| `findById(id)`                                   | Ищет сущность по идентификатору (возвращает Optional)         | `studentRepository.findById(1L);`             |
| `existsById(id)`                                 | Проверяет существование сущности с указанным ID               | `studentRepository.existsById(1L);`           |
| `findAll()`                                      | Возвращает все сущности                                       | `studentRepository.findAll();`                |
| `findAllById(Iterable<ID> ids)`                  | Возвращает все сущности с указанными ID                       | `studentRepository.findAllById(List.of(1L,2L));`|
| `count()`                                        | Возвращает количество сущностей в репозитории                 | `studentRepository.count();`                  |
| `delete(entity)`                                 | Удаляет переданную сущность                                   | `studentRepository.delete(student);`          |
| `deleteById(id)`                                 | Удаляет сущность по ID                                        | `studentRepository.deleteById(1L);`           |
| `deleteAll()`                                    | Удаляет все сущности из репозитория                           | `studentRepository.deleteAll();`              |
| `existsById(id)`                                 | Проверяет, существует ли сущность с указанным ID              | `studentRepository.existsById(1L);`           |


### **Собственные методы (Query methods)**

Spring Data позволяет добавлять собственные методы на основе именования методов (Derived Queries):

```java
public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByNameContaining(String partOfName);
}
```

Spring Data автоматически сгенерирует запрос JPQL:

```sql
select s from Student s where s.name like %?1%
```


Изменение репозитория StudentRepository с использованием CrudRepository

``` java 

@Repository
public interface StudentRepository  extends CrudRepository<Student, Long>{
    List<Student> findByName(String name);
}

```

## JpaRepository

JpaRepository — это интерфейс из проекта Spring Data JPA, который расширяет возможности интерфейса CrudRepository и предоставляет дополнительные методы для удобной работы с JPA.


| Метод                                                | Описание                                                 | Пример использования                                       |
|------------------------------------------------------|----------------------------------------------------------|------------------------------------------------------------|
| `List<T> findAll()`                               | Возвращает все сущности списком (`List`)                  | `List<Student> students = repository.findAll();`           |
| `List<T> findAll(Sort sort)`                      | Возвращает все сущности, отсортированные согласно переданному критерию  | `repository.findAll(Sort.by("name").ascending());`        |
| `Page<T> findAll(Pageable pageable)`              | Возвращает сущности с поддержкой пагинации                | `repository.findAll(PageRequest.of(0, 10));`               |
| `flush()`                                         | Немедленно синхронизирует состояние persistence context с БД (выполняет запросы)     | `repository.flush();`                                     |
| `saveAndFlush(entity)`                            | Сохраняет сущность и сразу выполняет flush (синхронизацию с БД) | `repository.saveAndFlush(student);`                        |
| `deleteAllInBatch()`                              | Удаляет все сущности одной операцией (одним запросом)     | `repository.deleteAllInBatch();`                           |
| `deleteAllInBatch(Iterable<T> entities)`          | Удаляет указанные сущности пакетом (batch) без дополнительных запросов на выборку | `repository.deleteAllInBatch(students);`                   |
| `deleteAllByIdInBatch(Iterable<ID> ids)`          | Удаление всех сущностей по идентификаторам одним запросом  | `repository.deleteAllByIdInBatch(List.of(1L,2L));`         |
| `getOne(id)` *(deprecated, используйте getReferenceById)* | Получает ссылку на сущность по ID, может выдавать Proxy без обращения к БД        | `repository.getReferenceById(1L);`                         |

Отличия CrudRepository от JpaRepository

| Возможности                   | CrudRepository                    | JpaRepository (расширяет CrudRepository) |
|--------------------------------|----------------------------------|------------------------------------------|
| CRUD-операции                  | ✅ Есть                           | ✅ Есть                             |
|  и сортировка         | ❌ Нет (базово нет)               | ✅ Поддерживается из коробки        |
| Методы `findAll()`             | ✅ Возвращает Iterable            | ✅ Возвращает List                  |
| Поддержка метода `flush()`     | ❌ Нет                            | ✅ Есть                             |
| Методы пакетного удаления (`deleteAllInBatch()`) | ❌ Нет                | ✅ Есть         |

Пример пагинации

Репозиторий

``` java
@Repository
public interface StudentRepository  extends JpaRepository<Student, Long>{
    List<Student> findByName(String name);
    Page<Student> findByNameContaining(String name, Pageable pageable);
}

```

Сервис

``` java

    public void findStudentByNamePageable(String name) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());

        var studentPage = studentRepository.findByNameContaining(name, pageable);

        LOGGER.info("Общее число записей: " + studentPage.getTotalElements());
        LOGGER.info("Всего страниц: " + studentPage.getTotalPages());
    
        List<Student> students = studentPage.getContent();
        students.forEach(student -> System.out.println(student.getName()));
    }
```


### Использование транзакции в Spring

Использование транзакций в Spring осуществляется через удобную и простую декларативную аннотацию @Transactional.

Эта аннотация позволяет автоматически управлять транзакциями, освобождая разработчика от необходимости вручную открывать, закрывать и управлять транзакциями.

``` java
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public void saveStudent(Student student) {
        studentRepository.save(student);
        // Транзакция завершится commit-ом после выполнения этого метода
    }
}
```


### Настройка поддержки транзакций

Чтобы использовать аннотацию @Transactional, необходимо сконфигурировать Spring для поддержки транзакций.
Аннотация @EnableTransactionManagement включает поддержку декларативных транзакций.

``` java

@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

```

### Основные атрибуты аннотации @Transactiona

| Атрибут аннотации    | Описание                                        | Пример использования                             |
|----------------------|-------------------------------------------------|--------------------------------------------------|
| `readOnly`           | Указывает транзакцию только для чтения (по умолчанию false) | `@Transactional(readOnly=true)`                  |
| `rollbackFor`        | Указывает, при каких исключениях делать откат (rollback)      | `@Transactional(rollbackFor=Exception.class)`    |
| `noRollbackFor`      | Исключения, при которых не делать rollback      | `@Transactional(noRollbackFor=CustomException.class)` |
| `timeout`            | Таймаут транзакции в секундах                   | `@Transactional(timeout=30)`                     |
| `propagation`        | Определяет поведение транзакции при её вызове из другого метода | `@Transactional(propagation=Propagation.REQUIRED)`|
| `isolation`          | Уровень изоляции транзакции                     | `@Transactional(isolation=Isolation.READ_COMMITTED)` |

Типы распространения транзакций (Propagation)


| Тип Propagation         | Описание поведения транзакции                       |
|-------------------------|------------------------------------------------------|
| `REQUIRED`              | Использует существующую транзакцию или создаёт новую |
| `REQUIRES_NEW`          | Всегда создаёт новую транзакцию (приостанавливая текущую) |
| `SUPPORTS`              | Выполняется в текущей транзакции, если она есть      |
| `NOT_SUPPORTED`         | Выполняется без транзакции (текущая приостанавливается) |
| `MANDATORY`             | Обязательно требует существующую транзакцию, иначе исключение |
| `NEVER`                 | Запрещает наличие транзакции (исключение, если есть транзакция) |
| `NESTED`                | Выполняется внутри вложенной транзакции (поддерживается не всеми менеджерами) |