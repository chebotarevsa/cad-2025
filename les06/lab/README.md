# Отчет о лаботаротоной работе

## Цель работы
Доработать приложение с помощью бд
## Выполнение работы
На гитхабе
## Выводы
Было доработано приложение добавлением к нему бд

Контрольные вопросы:
1. Что такое Spring JDBC и какие преимущества оно предоставляет по сравнению с традиционным JDBC?

Spring JDBC — это модуль фреймворка Spring, упрощающий работу с базами данных через JDBC.
Он инкапсулирует повторяющийся шаблонный код, связанный с открытием/закрытием соединений, подготовкой и выполнением SQL-запросов, обработкой исключений и т.д.

Преимущества Spring JDBC:

    Упрощение кода: не нужно вручную управлять ресурсами (Connection, Statement, ResultSet).

    Обработка исключений: все SQL-исключения преобразуются в унифицированные DataAccessException и её подклассы.

    Поддержка шаблона JdbcTemplate, который предоставляет удобные методы для CRUD-операций.

    Повышение читаемости и сопровождения кода.

2. Какой основной класс в Spring используется для работы с базой данных через JDBC?

Основной класс — JdbcTemplate.
3. Какие шаги необходимо выполнить для настройки JDBC в Spring-приложении?

    Добавить зависимости:

implementation 'org.springframework.boot:spring-boot-starter-jdbc'
runtimeOnly 'com.h2database:h2' // или другой драйвер БД

Настроить источник данных (DataSource):
В application.properties или application.yml:

spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=pass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

Создать бин JdbcTemplate (если не используется Spring Boot):

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

4. Что такое JdbcTemplate и какие основные методы он предоставляет?

JdbcTemplate — это класс, предоставляющий удобный API для выполнения SQL-запросов и обновлений.

Основные методы:

    query(...) — выполнение SELECT-запросов.

    queryForObject(...) — получение одного объекта.

    queryForList(...) — получение списка значений.

    update(...) — выполнение INSERT, UPDATE, DELETE.

    batchUpdate(...) — пакетное обновление.

    execute(...) — выполнение произвольных SQL-команд.

5. Как в Spring JDBC выполнить запрос на выборку данных (SELECT) и получить результат в виде объекта?

Пример:

String sql = "SELECT * FROM users WHERE id = ?";
User user = jdbcTemplate.queryForObject(sql, new Object[]{1}, new UserRowMapper());

Где UserRowMapper — это реализация RowMapper<User>, преобразующая строку из ResultSet в объект User.
6. Как использовать RowMapper в JdbcTemplate?

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        return user;
    }
}

Использование:

List<User> users = jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());

7. Как выполнить вставку (INSERT) данных в базу с использованием JdbcTemplate?

String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
int result = jdbcTemplate.update(sql, "John Doe", "john@example.com");

8. Как выполнить обновление (UPDATE) или удаление (DELETE) записей через JdbcTemplate?

Обновление:

String sql = "UPDATE users SET name = ? WHERE id = ?";
int result = jdbcTemplate.update(sql, "Jane Doe", 1);

Удаление:

String sql = "DELETE FROM users WHERE id = ?";
int result = jdbcTemplate.update(sql, 1);

9. Как в Spring JDBC обрабатывать исключения, возникающие при работе с базой данных?

Spring автоматически оборачивает SQL-исключения в подклассы DataAccessException. Можно использовать блок try-catch:

try {
    jdbcTemplate.update("INSERT INTO users ...");
} catch (DataAccessException e) {
    // логирование, обработка ошибки
    System.out.println("Ошибка при доступе к данным: " + e.getMessage());
}

10. Какие альтернативные способы работы с базой данных есть в Spring кроме JdbcTemplate?

    Spring Data JPA — абстракция над JPA/Hibernate, предоставляет CrudRepository, JpaRepository.

    Spring Data JDBC — более легковесная альтернатива JPA.

    Spring ORM — интеграция с ORM-фреймворками, такими как Hibernate.

    Spring R2DBC — для реактивного доступа к базам данных.

    Spring Data MongoDB / Redis / Cassandra — для NoSQL-хранилищ.