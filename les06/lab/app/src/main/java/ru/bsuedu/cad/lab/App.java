package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // Получаем бины рендерера и запроса категории
        Renderer renderer = ctx.getBean(DataBaseRenderer.class);
        renderer.render();

        CategoryRequest categoryRequest = ctx.getBean(CategoryRequest.class);
        categoryRequest.execute();  // Выполнение запроса для получения категорий
    }
}
