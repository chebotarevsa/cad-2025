package ru.bsuedu.cad.lab;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    @Value("${app.csv.filename}")
    private String csvFileName;

    @Value("${app.html.filename}")
    private String htmlFileName;

    @Bean
    public Resource csvResource() {
        return new ClassPathResource(csvFileName);
    }

    @Bean
    public Reader reader() {
        return new ResourceFileReader(csvResource());
    }

    @Bean
    public Parser parser() {
        var proxy = new ProxyFactory();
        var parser = new CSVParser();
        var execution = new ExecutionCSVParser();
        proxy.addAdvice(execution);
        proxy.setTarget(parser);
        return (Parser) proxy.getProxy();
    }

    @Bean
    public ProductProvider productProvider() {
        return new ConcreteProductProvider(reader(), parser());
    }

    @Bean
    public Renderer renderer() {
        System.out.println("Продукты успешно прочитаны в HTML-файл");
        return new HTMLTableRenderer(productProvider(), htmlFileName);
        // return new ConsoleTableRenderer(productProvider());
    }

}