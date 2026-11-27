package ru.bsuedu.cad.demo;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

import javax.sql.DataSource;

@Import(ConfigBasic.class)
@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
public class ConfigHibernate {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfigBasic.class);

    @Autowired
    DataSource dataSource;

    @Bean
    public Properties hibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.put(Environment.HBM2DDL_AUTO, "create-drop");
        hibernateProp.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        hibernateProp.put(Environment.FORMAT_SQL, true);
        hibernateProp.put(Environment.USE_SQL_COMMENTS, false);
        hibernateProp.put(Environment.SHOW_SQL, true);
        hibernateProp.put(Environment.MAX_FETCH_DEPTH, 3);
        hibernateProp.put(Environment.STATEMENT_BATCH_SIZE, 10);
        hibernateProp.put(Environment.STATEMENT_FETCH_SIZE, 50);
        return hibernateProp;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        var sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("ru.bsuedu.cad.demo.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
}
