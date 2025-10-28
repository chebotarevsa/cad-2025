package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfiguration {
    
    @Bean
    public Parser parser(){
        return new CSVParser();
    }

    @Bean
    public Reader reader(){
        return new ResourceFileReader();
    }

    @Bean
    public ProductProvider provider(){
        ProductProvider provider = new ConcreteProductProvider(reader(),parser());        
        return provider;
    }

    @Bean
    public Renderer renderer() {
        Renderer renderer = new ConsoleTableRenderer(provider());
        return renderer;
    } 
}