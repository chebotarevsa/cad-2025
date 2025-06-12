package ru.bsu.cad.lab.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.bsu.cad.lab.reader.ResourceFileReader;
import ru.bsu.cad.lab.reader.Reader;

@Configuration
@ComponentScan("ru.bsu.cad.lab")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        System.out.println("Creating H2 embedded database...");
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName("petstore_db")
            .addScript("classpath:schema.sql")
            .build();
    }

    @Bean
    @Qualifier("productReader")
    public Reader productReader(ResourceLoader resourceLoader) { // Возвращаем Reader
        ResourceFileReader reader = new ResourceFileReader(resourceLoader);
        reader.setFilePath("classpath:product.csv");
        return reader;
    }
    
    @Bean
    @Qualifier("categoryReader")
    public Reader categoryReader(ResourceLoader resourceLoader) { // Возвращаем Reader
        ResourceFileReader reader = new ResourceFileReader(resourceLoader);
        reader.setFilePath("classpath:category.csv");
        return reader;
    }
}
