# Отчет о лаботаротоной работе №2 Конфигурирование приложение Spring c помощью аннотаций. Применение AOП для логирования

## Цель работы
Обновить конфигурацию приложения при помощи аннотаций. Добавить класс реализующий представление данных в виде HTML 
и замерить скорость выполнения кода при помощи аспектно-ориентированного программирования (АОП).

## Выполнение работы
Перед выполнением работы был скопирован результат выполнения лабораторной работы № 1 в директорию /les04/lab/.

Для того чтобы конфигурирование приложения осуществлялось с помощью аннотаций был переписан класс ```Config.java```.

Теперь класс имеет следующую структуру:

```java

package ru.bsuedu.cad.lab;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
public class Config {

    @Bean
    public Parser parser(){
        var proxy = new ProxyFactory();
        var parser = new CSVParser();
        var perfomance = new PerfomanceAdvice();
        proxy.addAdvice(perfomance);
        proxy.setTarget(parser);
        return (Parser)proxy.getProxy();
    }
}

 ```


Для того чтобы приложение получало имя файла для загрузки продуктов из конфигурационного файла application.properties была использована аннотация @Value и механизм выражений SpEL.

```java

@Component
@PropertySource("application.properties")
public class FilePathProvider {
    @Value("${filename}")
    private String fileName;

    public String getFileName(){
        return fileName;
    }
}

 ```

Была добавлена имплементация интерфейса Renderer - HTMLTableRenderer предназначение которой заключается в выводе таблицы в HTML-файл. 

Листинг реализации класса ```HTMLTableRenderer.java```

```java

package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component("HTML")
public class HTMLTableRenderer implements Renderer {

    @Override
    public void render() {
        var product = productProvider.getProducts();
        System.out.println("HTML");
        System.out.println(product);
    }
    final private ProductProvider productProvider;
    public HTMLTableRenderer(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }
    
}

 ```

Чтобы при работе приложения вызывалась реализация новой имплементации интерфейса Renderer был модифицирован класс ```App.java```

```java

public class App {
    
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        Renderer renderer = ctx.getBean("HTML", Renderer.class);
        renderer.render();
    }
}

```

Используя сведения о жизненном цикле бина была выведена в консоль дата и время, когда бин ResourceFileReader был полностью инициализирован.
Для этого в класс ```ResourceFileReader.java``` был добавлен метод ``` init() ``` с аннотацией @PostConstruct

```java

    @PostConstruct
    private void init() {
        System.out.println(LocalDateTime.now());
    }

```

С помощью инструментов AOП было замерено время, которое тратиться на парсинг CSV файла.
Для этих целей был создан новый класс ```PerfomanceAdvice.java```

```java

public class PerfomanceAdvice implements MethodInterceptor{

    @Override
    @Nullable
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        var start = System.currentTimeMillis();
        var result = invocation.proceed();
        var finish = System.currentTimeMillis();
        var delta = finish - start;
        System.out.println("Execution time: " + delta);
        return result;
    }
    
}

```

Обновленная UML-диаграмма классов

``` mermaid

classDiagram
    note "Товары для зоомагазина"
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    ProductProvider <|.. ConcreteProductProvider
    ConcreteProductProvider o-- Parser
    ConcreteProductProvider o-- Reader
    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer
    ConsoleTableRenderer o-- ProductProvider
    ProductProvider .. Product
    Parser .. Product

    class  Product {
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

    class  Reader{
        + String read()
    }
    <<interface>> Reader

    class ResourceFileReader {
        + String read()
    }

    class  Parser{
        + List[Product] parse(String)
    }
    <<interface>> Parser

    class CSVParser {
        + List[Product] parse(String)
    }

    class  Renderer{
        +void render()
    }
    <<interface>> Renderer

    class ConsoleTableRenderer {
        - ProductProvider provider
        +void render()
    }


    class ProductProvider {
        + List[Product] getProducts()
    }
    <<interface>> ProductProvider

    class ConcreteProductProvider{
        - Reader reader
        - Parser parser
       + List[Product] getProducts()
    }

    class HTMLTableRenderer{
        + void render()
        + HTMLTableRenderer(ProductProvider productProvider)
    }


```

## Выводы
В ходе выполнения лабораторной работы я обновил конфигурацию приложения при помощи аннотаций, добавить класс реализующий представление данных в виде HTML 
и замерил скорость выполнения кода при помощи аспектно-ориентированного программирования (АОП).