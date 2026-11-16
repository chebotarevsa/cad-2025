package ru.bsuedu.cad.lab;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

// import org.springframework.context.annotation.Bean;

@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig
{
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
    }

    // @Bean
    // public Reader reader() {
    //     return new ResourceFileReader("product.csv");
    // }

    // @Bean
    // public Parser parser() {
    //     return new CSVParser();
    // }

    // @Bean
    // public ProductProvider productProvider(
    //         Reader reader,
    //         Parser parser
    // ) {
    //     return new ConcreteProductProvider(reader, parser);
    // }

    // @Bean
    // public Renderer renderer() {
    //     return new ConsoleTableRenderer();
    // }
}