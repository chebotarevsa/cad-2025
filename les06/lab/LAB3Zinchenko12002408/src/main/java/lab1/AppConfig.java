package lab1;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "lab1")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig {
}