package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
// import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

// @Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
@EnableAspectJAutoProxy
@PropertySource("application.properties")
public class App {
    public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        Renderer renderer = context.getBean(Renderer.class);
        renderer.render();
     }
}
