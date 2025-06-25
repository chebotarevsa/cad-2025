# Отчет о лаботаротоной работе №3

## Цель работы
Добавить возможность сохранения данных в базе данных, выполнять SQL запросы и выводить их результаты в логи.

## Выполнение работы

Первым делом был скопирован результат выполнения лабораторной работы № 2 в директорию /les06/lab/.

Для подключения к приложению встраиваемой базы данных была добавлена библиотека ```com.h2database```. Далее, для настроки базы данных был изменен класс ```Config.java``` следующим образом:

```java

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
public class Config {
    private static Logger LOGGER = LoggerFactory.getLogger(Config.class);
    
    @Bean
    public DataSource dataSource(){
         LOGGER.info("Конфигурация базы данных");
        try {
            var dbBuilder = new EmbeddedDatabaseBuilder();
            return dbBuilder.setType(EmbeddedDatabaseType.H2)
                    .setName("shop.db")
                    .addScripts("classpath:db/schema.sql")
                    .build();
        } catch (Exception e) {
            LOGGER.error("Встремая база данных не создана!", e);
            return null;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}

```

Чтобы создать две таблицы "Продукты" (PRODUCTS) и "Категории" (CATEGORIES) был написан SQL скрипт в файле ```schema.sql```

```sql

CREATE TABLE Categories (
    category_id  identity primary key,
    name varchar (255) not null,
    description varchar (255) not null
);

CREATE TABLE Products(
    product_id  identity primary key,
    name varchar (255) not null,
    description varchar (255) not null,
    category_id int,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    price decimal,
    stock_quantity int,
    image_url varchar,
    created_at datetime,
    updated_at datetime
);

```

Чтобы при старте приложения EmbeddedDatabaseBuilder выполнял скрипт ```schema.sql``` и создавал в базе данных таблицы CATEGORIES и PRODUCTS был добавлен метод:

```java

addScripts("classpath:db/schema.sql")

```


Для таблицы "Категории" был создан Java класс Category

```java

public class Category {
    private int categoryID;
    private String name;
    private String description;
    
    public Category(int categoryID, String name, String description) {
        this.categoryID = categoryID;
        this.name = name;
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

 ```

А так же был реализован класс ConcreteCategoryProvider, который предоставляет данные из CSV файла ```category.csv```.

```java

@Component("CategoryProvider")
public class ConcreteCategoryProvider extends ConcreteProvider<Category>{

    public ConcreteCategoryProvider(Reader reader, Parser<Category> parser, FilePathProvider pathProvider) {
        super(reader, parser, pathProvider.getFileNameCategory());
    }
}

```

Была добавлена имплементация интерфейса Renderer - DataBaseRenderer которая сохраняет данные считанные из SCV файлов в таблицы базы данных.

```java

@Component("DataBaseRenderer")
public class DataBaseRenderer implements Renderer{

    final private Provider<Category> categoryProvider;
    final private Provider<Product> productProvider;
    private static Logger LOGGER = LoggerFactory.getLogger(DataBaseRenderer.class);
    final private JdbcTemplate jdbcTemplate;

    public DataBaseRenderer(Provider<Category> categoryProvider, Provider<Product> productProvider, JdbcTemplate jdbcTemplate) {
        this.categoryProvider = categoryProvider;
        this.productProvider = productProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void render() {
        for (var c : categoryProvider.getItems()) {
            addCategory(c);
        }
        for (var p : productProvider.getItems()) {
            addProduct(p);
        }
        for (var c  : categoryRequest()) {
            //LOGGER.info(String.valueOf(c.getCategoryID()));
            LOGGER.info(c.getName());
        }

    }


    public void addCategory(Category category) {
        String sql = "INSERT INTO Categories (category_id, name, description)"
                +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, category.getCategoryID(), category.getName(), category.getDescription());
    }
    public void addProduct(Product product) {
        String sql = "INSERT INTO Products (product_id, name, description, category_id, price, stock_quantity, image_url, created_at, updated_at)"
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getProductID(), product.getName(), product.getDescription(), product.getCategoryID(),
        product.getPrice(), product.getStockQuantity(), product.getImageURL(), product.getCreatedAt(), product.getUpdatedAt());
    }

    private RowMapper<Category> categoryRowMapper() {
        return (rs, rowNum) -> new Category(
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description"));
    
    }
}

```

Укажем в классе ```App.java ``` что реализация DataBaseRenderer должна использоваться по умолчанию

```java

Renderer renderer = ctx.getBean("DataBaseRenderer", Renderer.class);
renderer.render();

```

Для того чтобы получить информацию о списке категорий количество товаров в которых больше единицы реализуем класс CategoryRequest, который будет выполнять запрос к базе данных.
Данная информация будет выводиться в консоль с помощью библиотеки для логирования logback с уровенем лога INFO.

```java

public List<Category> categoryRequest(){
        String sql = "SELECT * FROM Categories WHERE category_id IN (SELECT Products.category_id FROM Categories JOIN Products on Categories.category_id = Products.category_id GROUP BY Products.category_id HAVING COUNT(Products.category_id) > 1)";
        return jdbcTemplate.query(sql, categoryRowMapper());
    }

```


## Выводы

В ходе выполнения лабораторной работы я:
- добавил возможность сохранения данных в базе данных;
- добавил выполнение SQL запросов и вывод их результатов в логи.