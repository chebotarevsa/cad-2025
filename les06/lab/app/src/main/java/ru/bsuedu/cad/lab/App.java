package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // Осуществляется получение бинов (бинарных данных) рендерера и запроса категории.
        Renderer renderer = ctx.getBean(DataBaseRenderer.class);
        renderer.render();

        CategoryRequest categoryRequest = ctx.getBean(CategoryRequest.class);
        categoryRequest.execute();  // Реализация запроса на получение перечня категорий.
    }
}
