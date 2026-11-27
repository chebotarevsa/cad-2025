# Поддержка JDBC в Spring

## 🔴 Java Database Connectivity

JDBC (Java Database Connectivity) — это API от Oracle, предоставляющий стандартный способ взаимодействия приложений на Java
с реляционными базами данных. JDBC позволяет отправлять SQL-запросы, извлекать данные и выполнять обновления в базах данных.

JDBC работает по четырёхуровневой архитектуре:

+ JDBC API – стандартные классы и интерфейсы для работы с БД.
+ JDBC Driver Manager – управляет загрузкой драйверов для различных СУБД.
+ JDBC Driver – драйвер, реализующий взаимодействие с конкретной СУБД.
+ База данных – хранилище, с которым работает приложение.

### 🟢 Подключение к базе данных

Для соединения с базой данных необходимо загрузить драйвер и создать соединение:

``` java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "root";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Соединение установлено!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

###  🟢 Выполнение SQL-запросов

Для выполнения запросов используются объекты Statement, PreparedStatement и CallableStatement.

#### Выполнение простого запроса

``` java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

```

#### Использование PreparedStatement

Используется для безопасных запросов с параметрами.

Например, для вставки записей

``` java
try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (name, age) VALUES (?, ?)")) {
    pstmt.setString(1, "Alice");
    pstmt.setInt(2, 25);
    pstmt.executeUpdate();
}

```

Или для получения записей

``` java
        // Значение параметра для поиска
        int ageThreshold = 26;

        // SQL-запрос с параметром
        String sql = "SELECT id, name, age FROM users WHERE age > ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Установка значения параметра
        pstmt.setInt(1, ageThreshold);

        // Выполнение запроса
        ResultSet rs = pstmt.executeQuery();

        // Обработка результата
        System.out.println("Пользователи старше " + ageThreshold + " лет:");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            System.out.println("ID: " + id + ", Имя: " + name + ", Возраст: " + age);
        }

    }
}
```

#### Использование CallableStatement в JDBC для вызова хранимых процедур

CallableStatement в JDBC используется для вызова хранимых процедур и функций из базы данных.

Хранимая процедура — это предварительно сохранённый на сервере базы данных SQL-код,
который можно вызывать многократно. По сути, это как функция в программировании, 
только она выполняется на стороне базы данных.

Пример хранимой процедуры для добавления пользователя

``` sql

CREATE PROCEDURE AddUser(IN userName VARCHAR(50), IN userAge INT, OUT resultMessage VARCHAR(100))
BEGIN
    INSERT INTO users (name, age) VALUES (userName, userAge);
    SET resultMessage = CONCAT('User ', userName, ' added successfully!');
END //

```

Вызов хранимой процедуры.

``` java
import java.sql.*;

public class CallableStatementExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Подготавливаем вызов хранимой процедуры
            CallableStatement cstmt = conn.prepareCall("{call AddUser(?, ?, ?)}");

            // Устанавливаем входные параметры
            cstmt.setString(1, "Bob");
            cstmt.setInt(2, 30);

            // Регистрируем выходной параметр
            cstmt.registerOutParameter(3, Types.VARCHAR);

            // Выполняем хранимую процедуру
            cstmt.execute();

            // Получаем значение выходного параметра
            String resultMessage = cstmt.getString(3);
            System.out.println("Результат: " + resultMessage);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

```

## 🔴 Инфраструктура Spring JDBC

JDBC (Java Database Connectivity) является основным способом взаимодействия с базами данных в Java-приложениях. В Spring Framework поддержка JDBC интегрирована через несколько компонентов, что упрощает работу с базами данных, снижает уровень шума и повышает читаемость и надежность кода.

Spring предоставляет мощную инфраструктуру для работы с JDBC, что позволяет упростить процесс подключения, выполнения запросов, обработки ошибок и управления транзакциями.

Поддержка интерфейса JDBC в Spring разделена на шести пакетов, каждый из которых реализует определенные аспекты доступа к базе данных через интерфейс JDBC.

| Пакет                              | Описание                                                                 |
|------------------------------------|--------------------------------------------------------------------------|
| `org.springframework.jdbc.core`    | Основные классы и интерфейсы для работы с JDBC, включая `JdbcTemplate`, `NamedParameterJdbcTemplate`, `RowMapper`, обработку исключений и выполнение SQL-запросов. |
| `org.springframework.jdbc.datasource` | Пакет для работы с **DataSource** (источник данных), включая классы для настройки соединений и пулов соединений, таких как `DriverManagerDataSource` и `BasicDataSource`. |
| `org.springframework.jdbc.support`  | Классы для поддержки работы с JDBC, такие как `SQLExceptionTranslator` для преобразования исключений и `DataAccessException` для работы с ошибками. |
| `org.springframework.jdbc.transaction` | Пакет для работы с транзакциями, включая `DataSourceTransactionManager` для управления транзакциями и `TransactionTemplate` для программного управления транзакциями. |
| `org.springframework.jdbc.object` | Классы для объектно-ориентированного подхода к выполнению SQL-запросов, такие как `SqlQuery` и `SqlUpdate`, которые помогают при работе с результатами запросов. |
| `org.springframework.jdbc.connection` | Классы для работы с низкоуровневыми соединениями и ресурсами JDBC, такие как `ConnectionCallback` и `ConnectionFactory`. |

### 🟢 Основные компоненты инфраструктуры JDBC в Spring

+ **JdbcTemplate**: Это основное средство для работы с JDBC в Spring. Он инкапсулирует работу с соединениями, управляет обработкой исключений и упрощает выполнение SQL-запросов и обновлений.
+ **NamedParameterJdbcTemplate**: Расширение JdbcTemplate, которое позволяет использовать именованные параметры в SQL-запросах, что делает код более читаемым и безопасным.
+ **RowMapper**: Интерфейс для преобразования строк результатов SQL-запроса в объекты Java.
+ **DataSource**: Это абстракция для подключения к базе данных. Spring поддерживает несколько типов источников данных, включая подключение через пул соединений, что значительно повышает производительность при работе с большими приложениями.
+ **JdbcDaoSupport**: Класс, который упрощает работу с JdbcTemplate для реализации DAO (Data Access Object) паттерна.
+ **Transaction Management**: Spring предоставляет возможности для управления транзакциями, используя как декларативные, так и программные подходы. Это помогает гарантировать целостность данных и управлять транзакциями на уровне базы данных.

####  DataSource в Spring

DataSource — это интерфейс в Java, который используется для управления соединениями с базой данных. Он служит основным источником для получения соединений с базой данных, что позволяет приложению работать с базой данных через JDBC. В Spring DataSource является ключевым компонентом для настройки взаимодействия с базой данных.

Вместо того, чтобы создавать отдельные соединения с базой данных вручную каждый раз, когда это необходимо, Spring использует DataSource для централизованного управления соединениями. Это позволяет:

1. Повторное использование соединений: Управление пулом соединений позволяет эффективно использовать ресурсы.
2. Управление транзакциями: Совместно с механизмами транзакций в Spring (например, @Transactional), DataSource позволяет правильно работать с транзакциями на уровне соединений.
3. Производительность: Использование пула соединений улучшает производительность, так как соединение с базой данных — это относительно дорогая операция. Пул соединений позволяет многим запросам использовать уже открытые соединения, экономя ресурсы.

Spring поддерживает различные типы DataSource:

1. Basic DataSource — простое соединение с базой данных, обычно используемое для несложных приложений.
2. Apache DBCP (Database Connection Pooling) — пул соединений от Apache, который предоставляет более продвинутую настройку и возможность управления соединениями.
3. HikariCP — быстрый и высокоэффективный пул соединений, который стал предпочтительным в Spring Boot начиная с версии 2.x.
4. C3P0 — еще один пул соединений, который также поддерживается Spring.

Пример настройки

```java

public class ConfigDb {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfigDb.class);

    @Value("org.mariadb.jdbc.Driver")
    private String driverClassName;

    @Value("jdbc:mariadb://localhost:3306/musicdb?useSSL=false")
    private String url;

    @Value("username")
    private String username;

    @Value("$password")
    private String password;

    @SuppressWarnings("unchecked")
    public DataSource dataSource() {
        try {
            var dataSource = new SimpleDriverDataSource();
            Class<? extends Driver> driver = (Class<? extends Driver>) Class.forName(driverClassName);
            dataSource.setDriverClass(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            return dataSource;
        } catch (Exception e) {
            LOGGER.error("DBCP DataSource bean cannot be created!", e);
            return null;
        }
    }
}

```

Для настройки DataSource с использованием HikariCP

``` java

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");

        return new HikariDataSource(config);
    }
}
```

#### JdbcTemplate и NamedParameterJdbcTemplate

JdbcTemplate — это один из ключевых компонентов Spring Framework, предназначенный для упрощения работы с базой данных. Он предоставляет удобный и мощный API для выполнения SQL-запросов, обработки результатов и управления транзакциями, избавляя разработчика от необходимости вручную управлять ресурсами, такими как соединения с базой данных, и обработки исключений.

Преимущества JdbcTemplate:

+ Простота использования: Весь процесс работы с JDBC значительно упрощен. Вместо того, чтобы писать вручную код для получения соединения, создания и выполнения SQL-запросов, обработки исключений и освобождения ресурсов, Spring делает это за вас.
+ Обработка исключений: JdbcTemplate обрабатывает все исключения JDBC, преобразуя их в более удобные исключения Spring.
+ Управление ресурсами: Внутри JdbcTemplate используются механизмы управления ресурсами, такие как соединения, которые автоматически закрываются, избегая утечек памяти.

Без JdbcTemplate

``` java

@Override
    public Persona getPersonaById(Long id) {

        try {
           var connection =   dataSource.getConnection();
           var statement = connection.prepareStatement("SELECT * FROM personas WHERE id = ?");
           statement.setLong(1, id);
           var resultSet = statement.executeQuery();
           LOGGER.info(resultSet.toString());
           while (resultSet.next()) {
            Long personaId = resultSet.getLong(1);
            String name = resultSet.getString(2);
            String arcana = resultSet.getString(3);
            int level = resultSet.getInt(4);
            int strength = resultSet.getInt(5);
            int magic = resultSet.getInt(6);
            int endurance = resultSet.getInt(7);
            int agility = resultSet.getInt(8);
            int luck = resultSet.getInt(9);
            long characterId = resultSet.getLong(10);

            return new Persona(personaId, name,arcana, level,strength,magic,endurance,agility,luck, characterId);
        }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return new Persona();

    }
```

С использование JdbcTemplate

``` java
    public void updatePersona(Persona persona) {
        String sql = "UPDATE personas SET name = ?, arcana = ?, level = ?, strength = ?, magic = ?, endurance = ?, " +
                "agility = ?, luck = ?, character_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, persona.getName(), persona.getArcana(), persona.getLevel(), persona.getStrength(),
                persona.getMagic(), persona.getEndurance(), persona.getAgility(), persona.getLuck(),
                persona.getCharacterId(), persona.getId());
    }

``` 

NamedParameterJdbcTemplate — это расширение JdbcTemplate, которое добавляет поддержку именованных параметров в SQL-запросах. Вместо использования обычных параметров через знак вопроса (?), как в JdbcTemplate, NamedParameterJdbcTemplate позволяет использовать именованные параметры, что улучшает читаемость кода, особенно в сложных запросах.

Преимущества NamedParameterJdbcTemplate:

+ Читаемость кода: Именованные параметры делают SQL-запросы более читаемыми, особенно когда количество параметров велико.
+ Удобство для сложных запросов: При использовании NamedParameterJdbcTemplate нет необходимости беспокоиться о порядке параметров, поскольку их можно ссылаться по имени.
+ Поддержка карты параметров: Вместо того чтобы передавать параметры как массив объектов, вы можете передавать их через карту, что позволяет легко управлять именованными параметрами.

``` java
    public void updatePersonaNamed(Persona persona) {
        String sql = "UPDATE personas SET name = :name, arcana = ?, level = ?, strength = ?, magic = ?, endurance = ?, " +
                "agility = ?, luck = ?, character_id = ? WHERE id = ?";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", persona.getName())
                .addValue("arcana", persona.getArcana())
                .addValue("strength", persona.getStrength())
                .addValue("magic", persona.getMagic())
                .addValue("agility", persona.getAgility())
                .addValue("luck", persona.getLuck())
                .addValue("character_id", persona.getCharacterId())
                .addValue("id", persona.getId());
        namedParameterJdbcTemplate.update(sql, params);
```

#### RowMapper

RowMapper — это функциональный интерфейс, предоставляемый Spring, который используется для преобразования строк из результата SQL-запроса в объекты Java. Он является ключевым компонентом для работы с базой данных в Spring и позволяет упростить процесс маппинга данных, полученных из базы данных, в объекты.

При выполнении SQL-запросов с помощью JdbcTemplate или NamedParameterJdbcTemplate результат обычно представляет собой набор строк (объект ResultSet). Эти строки нужно конвертировать в объекты Java, с которыми проще работать в приложении. RowMapper позволяет вам указать, как каждую строку из результата запроса нужно преобразовать в объект.

RowMapper имеет один метод mapRow(), который будет вызван для каждой строки результата запроса. Этот метод получает ResultSet и номер строки (индекс), и возвращает объект Java, который будет сохранен в список.

Пример реализации RowMapper

```java
    private RowMapper<Persona> personaRowMapper() {
        return (rs, rowNum) -> new Persona(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("arcana"),
                rs.getInt("level"),
                rs.getInt("strength"),
                rs.getInt("magic"),
                rs.getInt("endurance"),
                rs.getInt("agility"),
                rs.getInt("luck"),
                rs.getLong("character_id"));
    }
```

#### JdbcDaoSupport

JdbcDaoSupport — это абстрактный класс в Spring, который упрощает использование JdbcTemplate для доступа к базе данных. Этот класс предоставляет поддержку для работы с JdbcTemplate, что позволяет вам сосредоточиться на бизнес-логике и не заботиться о настройке соединений или обработке ошибок.

JdbcDaoSupport автоматически инжектирует JdbcTemplate и управляет его конфигурацией, позволяя использовать его в вашем DAO-слое. Вы можете создать класс, наследующий JdbcDaoSupport, и воспользоваться преимуществами этого класса для выполнения запросов.

```java
@Repository("personaDaoJdbcTemplate")
public class PersonaJdbcDaoSupport  extends JdbcDaoSupport implements PersonaDao {

    // Получение персоны по ID
    @Override
    public Persona getPersonaById(Long id) {
        String sql = "SELECT * FROM personas WHERE id = ?";
        return getJdbcTemplate().queryForObject(sql, new Object[] { id }, personaRowMapper());
    }

    // RowMapper для преобразования результата запроса в объект Persona
    private RowMapper<Persona> personaRowMapper() {
        return (rs, rowNum) -> new Persona(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("arcana"),
                rs.getInt("level"),
                rs.getInt("strength"),
                rs.getInt("magic"),
                rs.getInt("endurance"),
                rs.getInt("agility"),
                rs.getInt("luck"),
                rs.getLong("character_id"));
    }
}
```