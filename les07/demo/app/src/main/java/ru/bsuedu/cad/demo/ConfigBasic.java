package ru.bsuedu.cad.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
@PropertySource("classpath:db/jdbc.properties")
public class ConfigBasic {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfigBasic.class);


    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        try {
            var hc = new HikariConfig();
            hc.setJdbcUrl(url);
            hc.setDriverClassName(driverClassName);
            hc.setUsername(username);
            hc.setPassword(password);
            var dataSource= new HikariDataSource(hc);
            dataSource.setMaximumPoolSize(25); // 25 is a good enough data pool size, it is a database in a container after all
            return dataSource;
        } catch (Exception e) {
            LOGGER.error("Hikari DataSource bean cannot be created!", e);
            return null;
        }
    }
}
