package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Reader reader() {
        return new ResourceFileReader("products.csv");
    }

    @Bean
    public Parser parser() {
        return new CSVParser();
    }

    @Bean
    public ProductProvider productProvider(Reader reader, Parser parser) {
        return new ConcreteProductProvider(reader, parser);
    }

    @Bean
    public Renderer renderer(ProductProvider productProvider) {
        return new ConsoleTableRenderer(productProvider);
    }
}