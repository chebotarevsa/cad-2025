package ru.bsuedu.cad.lab.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.CsvDataLoader;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "ru.bsuedu.cad.lab.service",
        "ru.bsuedu.cad.lab.repository"
})
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.lab.repository")
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.bsuedu.cad.lab.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        var props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.setProperty("hibernate.show_sql", "true");

        em.setJpaProperties(props);
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public CsvDataLoader csvDataLoader(CategoryRepository categoryRepository,
                                       CustomerRepository customerRepository,
                                       ProductRepository productRepository) {
        return new CsvDataLoader(categoryRepository, customerRepository, productRepository);
    }
}
