package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    Reader reader() {
        return new ResourceFileReader();
    }

    @Bean
    Parser parser() {
        return new CSVParser();
    }

    @Bean
    ProductProvider productProvider(Reader reader, Parser parser) {
        return new ConcreteProductProvider(reader, parser);
    }

    @Bean
    Renderer renderer(ProductProvider productProvider) {
        return new ConsoleTableRenderer(productProvider);
    }
}
