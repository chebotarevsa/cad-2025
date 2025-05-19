package ru.bsuedu.cad.lab.config;

import ru.bsuedu.cad.lab.parser.*;
import ru.bsuedu.cad.lab.provider.*;
import ru.bsuedu.cad.lab.reader.*;
import ru.bsuedu.cad.lab.renderer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Reader reader() {
        return new ResourceFileReader("product.csv");
    }

    @Bean
    public Parser parser() {
        return new CSVParser();
    }

    @Bean
    public ProductProvider productProvider() {
        return new ConcreteProductProvider(reader(), parser());
    }

    @Bean
    public Renderer renderer() {
        return new ConsoleTableRenderer(productProvider());
    }
}
