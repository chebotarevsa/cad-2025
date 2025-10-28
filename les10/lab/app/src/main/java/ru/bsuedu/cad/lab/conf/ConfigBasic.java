package ru.bsuedu.cad.lab.conf;

import org.hibernate.cfg.Environment;
import org.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.*;
import com.zaxxer.hikari.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
@PropertySource("classpath:db/db.properties")
@EnableJpaRepositories(basePackages = "ru.bsuedu.cad.lab.repository")
@EnableTransactionManagement
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
      var dataSource = new HikariDataSource(hc);
      dataSource.setMaximumPoolSize(25);
      return dataSource;
    } catch (Exception e) {
      LOGGER.error("Hikari DataSource bean cannot be created!", e);
      return null;
    }
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());

    em.setPackagesToScan("ru.bsuedu.cad.lab.entity");

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(true);
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
    em.setJpaVendorAdapter(vendorAdapter);

    // Дополнительные свойства JPA/Hibernate
    Properties properties = new Properties();
    properties.put(Environment.HBM2DDL_AUTO, "create-drop");
    properties.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
    properties.put(Environment.FORMAT_SQL, false);
    properties.put(Environment.USE_SQL_COMMENTS, false);
    properties.put(Environment.SHOW_SQL, false);
    properties.put(Environment.MAX_FETCH_DEPTH, 3);
    properties.put(Environment.STATEMENT_BATCH_SIZE, 10);
    properties.put(Environment.STATEMENT_FETCH_SIZE, 50);
    properties.put(Environment.HBM2DDL_IMPORT_FILES, "data.sql");
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
