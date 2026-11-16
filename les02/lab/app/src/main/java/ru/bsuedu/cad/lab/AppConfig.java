package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;

public class AppConfig
{
    @Bean
    public Reader reader() {
        return new ResourceFileReader("product.csv");
    }

    @Bean
    public Parser parser() {
        return new CSVParser();
    }

    @Bean
    public ProductProvider productProvider(
            Reader reader,
            Parser parser
    ) {
        return new ConcreteProductProvider(reader, parser);
    }

    @Bean
    public Renderer renderer() {
        return new ConsoleTableRenderer();
    }
}