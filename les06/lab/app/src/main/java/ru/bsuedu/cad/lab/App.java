package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.text.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Renderer renderer = context.getBean("databaseRenderer",Renderer.class);
        renderer.render();
     }
}

// Реализуйте класс CategoryRequest, данный класс должен выполнять запрос к базе данных получающий следующую информацию - список категорий количество товаров в которых больше единицы.+

// Приложение должно запускаться с помощью команды gradle run, выводить необходимую информацию в консоль и успешно завершаться.