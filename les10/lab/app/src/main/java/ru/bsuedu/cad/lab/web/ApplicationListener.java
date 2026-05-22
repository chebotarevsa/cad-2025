package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.ConfigJpa;
import ru.bsuedu.cad.lab.service.DataLoader;

public class ApplicationListener implements ServletContextListener {
    public static final String SPRING_CONTEXT = "springContext";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ConfigJpa.class);
        context.getBean(DataLoader.class).loadData();
        event.getServletContext().setAttribute(SPRING_CONTEXT, context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        AnnotationConfigApplicationContext context =
                (AnnotationConfigApplicationContext) event.getServletContext().getAttribute(SPRING_CONTEXT);
        if (context != null) {
            context.close();
        }
    }
}
