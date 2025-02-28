package ru.bsuedu.cad.lab;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.h2.Driver;


@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
public class Config {
    private static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    @Bean
    public DataSource dataSource() {
        LOGGER.info("Конфигурация базы данных");
        try {
            var dbBuilder = new EmbeddedDatabaseBuilder();
            return dbBuilder.setType(EmbeddedDatabaseType.H2)
                    .setName("persona.db")
                    .addScripts("classpath:db/schema.sql",
                            "classpath:db/data.sql")
                    .build();
        } catch (Exception e) {
            LOGGER.error("Встремая база данных не создана!", e);
            return null;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(dataSource());
    }
}
