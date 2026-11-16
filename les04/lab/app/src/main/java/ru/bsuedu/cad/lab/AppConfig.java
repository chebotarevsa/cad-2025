package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

// import org.springframework.context.annotation.Bean;


@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")


public class AppConfig
{
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