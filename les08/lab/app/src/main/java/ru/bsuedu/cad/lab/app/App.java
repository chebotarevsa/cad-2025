package ru.bsuedu.cad.lab.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.AppConfiguration;

public class App {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
        OrderClient client = ctx.getBean(OrderClient.class);
        client.run();
        ctx.close();
    }
}
