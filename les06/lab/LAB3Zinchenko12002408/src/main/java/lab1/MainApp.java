package lab1;

import lab1.service.DatabaseLogger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext ctx =
                     new AnnotationConfigApplicationContext(AppConfig.class, DatabaseConfig.class)) {

            DatabaseLogger logger = ctx.getBean(DatabaseLogger.class);
            logger.logProducts();
        }
    }
}