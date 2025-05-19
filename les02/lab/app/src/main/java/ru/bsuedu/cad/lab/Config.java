package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    Reader reader() {
        return new ResourceFileReader();
        
    }
    @Bean
    Parser parser() {
        return new CSVParser();
        
    }
    @Bean
    ProductProvider productProvider() {
        return new ConcreteProductProvider(reader(), parser());
        
    }
    @Bean
    Renderer renderer() {
        return new ConsoleTableRenderer(productProvider());
        
    }
}
