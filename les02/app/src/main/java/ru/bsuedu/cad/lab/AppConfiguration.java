package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.bsuedu.cad.lab.impl.CSVParser;
import ru.bsuedu.cad.lab.impl.ConcreteProductProvider;
import ru.bsuedu.cad.lab.impl.ConsoleTableRenderer;
import ru.bsuedu.cad.lab.impl.ResourceFileReader;

@Configuration
public class AppConfiguration {

    @Bean
    Reader reader() {
        return new ResourceFileReader("product.csv");
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
