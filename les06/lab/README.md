# Отчет о лаботаротоной работе

## Цель работы

Научиться подключать и использовать встроенные базы данных в Java-приложениях на базе Spring Framework. Освоить работу с JDBC через Spring: DataSource, JdbcTemplate, RowMapper, а также внедрение данных в таблицы и выполнение SQL-запросов.


## Выполнение работы

### 1. Копирование проекта

Исходный проект из лабораторной №4 был скопирован в директорию `les06/lab`. Все классы, ресурсы и структура проекта были сохранены.

---

### 2. Подключение встроенной базы данных H2

В проект добавлена зависимость:

implementation("com.h2database:h2:2.2.224")
---
Создан конфигурационный класс:

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
Файл schema.sql был добавлен в src/main/resources, создающий таблицы CATEGORIES и PRODUCTS.

3. SQL-скрипт для создания таблиц

DROP TABLE IF EXISTS PRODUCTS;
DROP TABLE IF EXISTS CATEGORIES;

CREATE TABLE CATEGORIES (
    category_id INT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT
);

CREATE TABLE PRODUCTS (
    product_id INT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    category_id INT,
    price DECIMAL(10, 2),
    stock_quantity INT,
    image_url VARCHAR(512),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
);

4. Автоматическое создание базы и таблиц
Скрипт schema.sql автоматически выполняется при старте приложения благодаря EmbeddedDatabaseBuilder.

5. Модель и провайдер категорий
Создан класс Category и интерфейс CategoryProvider. Имплементация ConcreteCategoryProvider считывает данные из category.csv, расположенного в src/main/resources.

6. Реализация DataBaseRenderer
Создан класс DataBaseRenderer, который сохраняет данные из CSV-файлов в H2-базу через JdbcTemplate. Он помечен как @Primary, чтобы использоваться вместо ConsoleTableRenderer.


@Component
@Primary
public class DataBaseRenderer implements Renderer {
    ...
}

7. SQL-запрос через CategoryRequest
Создан компонент CategoryRequest, выполняющий SQL-запрос, который выбирает все категории, у которых больше одного товара. Информация выводится в лог с уровнем INFO:

logger.info("Категории с более чем одним товаром:");
results.forEach(name -> logger.info("→ {}", name));

8. Запуск через gradle run
Приложение успешно запускается, выводит сообщения в консоль, сохраняет данные в H2 и выполняет SQL-запросы.

9. UML-диаграмма классов

classDiagram
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

    class Category {
        +int categoryId
        +String name
        +String description
    }

    interface Reader {
        +String read()
    }

    class ResourceFileReader {
        +String read()
    }

    interface Parser {
        +List~Product~ parse(String)
    }

    class CSVParser {
        +List~Product~ parse(String)
    }

    interface ProductProvider {
        +List~Product~ getProducts()
    }

    class ConcreteProductProvider {
        -Reader reader
        -Parser parser
        +List~Product~ getProducts()
    }

    interface CategoryProvider {
        +List~Category~ getCategories()
    }

    class ConcreteCategoryProvider {
        +List~Category~ getCategories()
    }

    interface Renderer {
        +void render()
    }

    class DataBaseRenderer {
        +void render()
    }

    class CategoryRequest {
        +void logCategoriesWithMultipleProducts()
    }

    Product --> Category
    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser
    ConcreteCategoryProvider --> Reader
    DataBaseRenderer --> ProductProvider
    DataBaseRenderer --> CategoryProvider
    CategoryRequest --> JdbcTemplate


## Выводы
В рамках лабораторной работы: 
    Освоены инструменты работы с базой данных в Spring (DataSource, JdbcTemplate)
    Подключена и настроена H2 база данных через EmbeddedDatabaseBuilder
    Реализовано сохранение данных в таблицы через рендерер
    Выполнен SQL-запрос к базе и результат выведен в лог
