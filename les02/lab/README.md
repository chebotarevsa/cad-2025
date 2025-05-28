# Отчет о лаботаротоной работе

## Цель работы

Изучить и применить на практике принципы объектно-ориентированного проектирования и внедрения зависимостей (DI) с использованием фреймворка Spring. Разработать консольное приложение, конфигурируемое с помощью Java-конфигурации, для отображения списка товаров зоомагазина, прочитанного из CSV-файла.

## Выполнение работы
Архитектура приложения:
Построена по заранее определённой UML-диаграмме, включающей интерфейсы (Reader, Parser, ProductProvider, Renderer) и их реализации (ResourceFileReader, CSVParser, ConcreteProductProvider, ConsoleTableRenderer).
Использован подход инверсии зависимостей (Dependency Injection), реализованный через фреймворк Spring.
Все зависимости настраиваются с помощью Java-based конфигурации (@Configuration, @Bean).

Работа с данными:

В качестве источника данных использован заранее предоставленный CSV-файл с информацией о товарах зоомагазина.
Механизм чтения реализован в ResourceFileReader, а разбор данных — в CSVParser.

Интеграция компонентов:

ConcreteProductProvider связывает чтение и парсинг.
ConsoleTableRenderer использует ProductProvider для отображения информации в табличной форме в консоли.

Организация проекта:

Код структурирован по пакетам в Gradle-проекте.
Использован Spring Context для инициализации приложения.
Все зависимости настроены без XML — исключительно через Java-классы.

Отладка и тестирование:
Устранены ошибки сборки, связанные с импортами и доступностью классов.
Убедились в работоспособности приложения и корректности вывода.

1. Модель Product
public class Product {
    private long productId;
    private String name;
    private String description;
    private int categoryId;
    private BigDecimal price;
    private int stockQuantity;
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;
    
    // Конструктор, геттеры, сеттеры, toString()
}
2. Чтение CSV — ResourceFileReader
public class ResourceFileReader implements Reader {
    private final String fileName;

    public ResourceFileReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String read() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new FileNotFoundException("Файл не найден: " + fileName);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла", e);
        }
    }
}
3. Парсинг CSV — CSVParser
public class CSVParser implements Parser {
    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();
        String[] lines = data.split("\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            Product product = new Product(
                Long.parseLong(parts[0]),
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                new BigDecimal(parts[4]),
                Integer.parseInt(parts[5]),
                parts[6],
                Date.valueOf(LocalDate.parse(parts[7], formatter)),
                Date.valueOf(LocalDate.parse(parts[8], formatter))
            );
            products.add(product);
        }
        return products;
    }
}
4. Поставка товаров — ConcreteProductProvider
public class ConcreteProductProvider implements ProductProvider {
    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        String csvData = reader.read();
        return parser.parse(csvData);
    }
}
5. Отображение таблицы — ConsoleTableRenderer
public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        System.out.printf("%-3s | %-25s | %-10s | %-8s\n", "ID", "Название", "Цена", "Остаток");
        System.out.println("----------------------------------------------------------");
        for (Product p : products) {
            System.out.printf("%-3d | %-25s | %-10s | %-8d\n",
                p.getProductId(), p.getName(), p.getPrice(), p.getStockQuantity());
        }
    }
}
6. Java-конфигурация — AppConfig
public class AppConfig {

    @Bean
    public Reader reader() {
        return new ResourceFileReader("product.csv");
    }

    @Bean
    public Parser parser() {
        return new CSVParser();
    }

    @Bean
    public ProductProvider productProvider() {
        return new ConcreteProductProvider(reader(), parser());
    }

    @Bean
    public Renderer renderer() {
        return new ConsoleTableRenderer(productProvider());
    }
}
7. Точка входа — App.java
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Renderer renderer = context.getBean(Renderer.class);
        renderer.render();
    }
}

## Выводы
Было успешно реализовано модульное консольное приложение, в котором применены основные возможности Spring Framework: управление зависимостями и Java-конфигурация. Работа позволила закрепить навыки построения гибкой архитектуры, настройки контекста Spring, чтения и отображения данных, а также работы со структурами проекта на Gradle. Такой подход облегчает масштабирование и тестирование приложения в будущем.