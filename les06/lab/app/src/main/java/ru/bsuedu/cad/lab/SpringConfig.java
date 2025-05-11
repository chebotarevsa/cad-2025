package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan(basePackages = "ru.bsuedu.cad.lab")
public class SpringConfig {

	@Bean
	public DataSource dataSource() {
		System.out.println("=== === DATABASE INITIALIZATION === ===");
		System.out.println("1. CREATING DATABASE");

		try {
			EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.setName("store.db")
					.addScripts("classpath:db/schema.sql")
					.build();

			System.out.println("2. DATABASE CREATED!");
			return db;
		} catch (Exception e) {
			System.err.println("ERROR creating database:");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
