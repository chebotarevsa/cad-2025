package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.AppConfig;
import ru.bsuedu.cad.lab.service.ProductProvider;
import ru.bsuedu.cad.lab.service.Renderer;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        var provider = context.getBean(ProductProvider.class);
        var renderer = context.getBean(Renderer.class);
        renderer.render(provider.getProducts());
        context.close();
    }
}
