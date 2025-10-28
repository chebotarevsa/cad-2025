package ru.bsuedu.cad.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@SpringBootApplication
@ComponentScan("ru.bsuedu.cad.lab")
public class Main {
    public static void main(String[] args) {
        // Настройка H2 БД
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        builder.addScript("classpath:schema.sql");
        DataSource dataSource = builder.build();

        // Spring Boot автоматически создаст JdbcTemplate
        SpringApplication app = new SpringApplication(Main.class);
        app.setDefaultProperties(
                java.util.Collections.singletonMap("spring.datasource.hikari.data-source", dataSource));
        var context = app.run(args);

        try {
            ProductProvider provider = context.getBean(ProductProvider.class);
            Renderer renderer = context.getBean("dbRenderer", Renderer.class);
            renderer.render(provider.getProducts());

            CategoryRequest categoryRequest = context.getBean(CategoryRequest.class);
            categoryRequest.executeQuery();
        } finally {
            context.close();
        }
    }
}