package ru.bsuedu.cad.lab;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    @Value("${app.csv.filename}")
    private String csvFileName;

    @Value("${app.html.filename}")
    private String htmlFileName;

    @Value("${app.category.csv.filename}")
    private String categoryCsvFileName;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    public Resource csvResource() {
        return new ClassPathResource(csvFileName);
    }

    @Bean
    public Resource categoryCsvResource() {
        return new ClassPathResource(categoryCsvFileName);
    }

    @Bean
    public Reader reader() {
        return new ResourceFileReader(csvResource());
    }

    @Bean
    public Reader categoryReader() {
        return new ResourceFileReader(categoryCsvResource());
    }

    @Bean
    public Parser parser() {
        var proxy = new ProxyFactory();
        var parser = new CSVParser();
        var execution = new ExecutionCSVParser();
        proxy.addAdvice(execution);
        proxy.setTarget(parser);
        return (Parser) proxy.getProxy();
    }

    @Bean
    public ProductProvider productProvider() {
        return new ConcreteProductProvider(reader(), parser());
    }

    @Bean
    public CategoryProvider categoryProvider() {
        return new ConcreteCategoryProvider(categoryReader(), parser());
    }

    @Bean
    public Renderer renderer() {
        return new DataBaseRenderer(productProvider(), categoryProvider(), dataSource());
    }

    @Bean
    public CategoryRequest categoryRequest() {
        return new CategoryRequest(dataSource());
    }
}