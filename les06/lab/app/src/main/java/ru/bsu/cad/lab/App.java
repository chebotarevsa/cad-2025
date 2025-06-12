package ru.bsu.cad.lab;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.bsu.cad.lab.config.AppConfig;
import ru.bsu.cad.lab.renderer.Renderer;
import ru.bsu.cad.lab.request.CategoryRequest;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class App {
    public static void main(String[] args) {
        System.out.println("Запуск приложения...");
        try {
            AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfig.class);
            
            // Проверка бинов
            System.out.println("Проверка бинов:");
            System.out.println("Renderer: " + context.getBean(Renderer.class).getClass().getSimpleName());
            System.out.println("DataSource: " + context.getBean(DataSource.class));
            
            Renderer renderer = context.getBean(Renderer.class);
            renderer.render();
            
            CategoryRequest request = context.getBean(CategoryRequest.class);
            request.printCategoriesWithProducts();
            // Проверка данных в БД
            JdbcTemplate jdbc = context.getBean(JdbcTemplate.class);
            List<Map<String, Object>> categories = jdbc.queryForList("SELECT * FROM CATEGORIES");
            List<Map<String, Object>> products = jdbc.queryForList("SELECT * FROM PRODUCTS");
            System.out.println("Categories in DB: " + categories.size());
            System.out.println("Products in DB: " + products.size());
            context.close();
            System.out.println("Приложение завершено успешно");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}