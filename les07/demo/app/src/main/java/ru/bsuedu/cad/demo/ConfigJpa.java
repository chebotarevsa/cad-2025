package ru.bsuedu.cad.demo;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

import java.util.Properties;
import javax.sql.DataSource;

@Import(ConfigBasic.class)
//@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.demo")
@EnableTransactionManagement
public class ConfigJpa {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfigBasic.class);

    @Autowired
    DataSource dataSource;


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = 
            new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        
        em.setPackagesToScan("ru.bsuedu.cad.demo.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        em.setJpaVendorAdapter(vendorAdapter);

        // Дополнительные свойства JPA/Hibernate
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "create-drop");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.USE_SQL_COMMENTS, false);
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.MAX_FETCH_DEPTH, 3);
        properties.put(Environment.STATEMENT_BATCH_SIZE, 10);
        properties.put(Environment.STATEMENT_FETCH_SIZE, 50);
        em.setJpaProperties(properties);

        return em;
    }
    @Bean
    public PlatformTransactionManager transactionManager(
        @Autowired EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
