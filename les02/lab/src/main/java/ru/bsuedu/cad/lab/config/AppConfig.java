package ru.bsuedu.cad.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bsuedu.cad.lab.service.*;

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
    public ProductProvider provider() {
        return new ConcreteProductProvider(reader(), parser());
    }

    @Bean
    public Renderer renderer() {
        return new ConsoleTableRenderer();
    }
}
