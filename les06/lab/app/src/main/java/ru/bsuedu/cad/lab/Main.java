package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

@ComponentScan("ru.bsuedu.cad.lab")
public class Main {
    public static void main(String[] args) {
        // Настройка H2 БД
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        builder.addScript("classpath:schema.sql");
        DataSource dataSource = builder.build();

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class)) {
            context.getBeanFactory().registerSingleton("dataSource", dataSource);

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            context.getBeanFactory().registerSingleton("jdbcTemplate", jdbcTemplate);

            ProductProvider provider = context.getBean(ProductProvider.class);
            Renderer renderer = context.getBean("dbRenderer", Renderer.class);
            renderer.render(provider.getProducts());

            CategoryRequest categoryRequest = context.getBean(CategoryRequest.class);
            categoryRequest.executeQuery();
        }
    }
}