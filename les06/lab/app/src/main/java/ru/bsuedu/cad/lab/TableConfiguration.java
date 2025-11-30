package ru.bsuedu.cad.lab;


import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")


public class TableConfiguration {
    @Bean
    public Parser Parser(){
        // var CSVParser = new CSVParser(); return CSVParser;
        var proxy = new ProxyFactory();
        var CSVParser = new CSVParser();
        var advice = new TimeCalculate();
        var pointcut = new TimeCalculatePointCut();
        var advisor = new DefaultPointcutAdvisor(pointcut, advice);

        proxy.addAdvisor(advisor);
        proxy.setTarget(CSVParser);

        return (Parser)proxy.getProxy();
    }

    @Bean
    public DataSource dataSource() {
        System.out.println("Config DataBase");
        
        // LOGGER.info("Конфигурация базы данных");
        try {
            var dbBuilder = new EmbeddedDatabaseBuilder();
            return dbBuilder.setType(EmbeddedDatabaseType.H2)
                    .setName("persona.db")
                    .addScripts("classpath:db/schema.sql")
                    .build();
        } catch (Exception e) {
            System.out.println("DataBase didnt create");
            e.printStackTrace();
            // LOGGER.error("Встремая база данных не создана!", e);
            return null;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        
        return new JdbcTemplate(dataSource());
    }




   
}
