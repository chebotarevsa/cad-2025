package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import ru.bsuedu.cad.lab.service.ProductProvider;
import ru.bsuedu.cad.lab.service.Renderer;

public class Main {
    public static void main(String[] args) {
        // Создаем контекст вручную
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // Загружаем application.properties
        var propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyConfigurer.setLocation(new ClassPathResource("application.properties"));
        context.addBeanFactoryPostProcessor(propertyConfigurer);

        // Сканируем пакет
        context.scan("ru.bsuedu.cad.lab");
        context.refresh();

        // Получаем бины
        var provider = context.getBean(ProductProvider.class);
        var renderer = context.getBean(Renderer.class);
        renderer.render(provider.getProducts());

        // Завершаем работу
        context.close();
    }
}
