package ru.bsuedu.cad.lab;

import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


@Configuration
@ComponentScan(
   basePackages = {"ru.bsuedu.cad.lab"}
)

public class Config {

   @Bean
   public Parser parser() {
      //var csvParser = new CSVParser(); return csvParser;
      var proxy = new ProxyFactory();
      var csvParser = new ProductCSVParser();
      var advice = new TimeAround();
      var pointcut = new TimeAroundPointCut();
      var advisor = new DefaultPointcutAdvisor(pointcut, advice);

      proxy.addAdvisor(advisor);
      proxy.setTarget(csvParser);

      return (Parser)proxy.getProxy();
   }   


   @Bean
    public DataSource dataSource() {
         System.out.println("Config DataBases");
        //LOGGER.info("Конфигурация базы данных");
        try {
            var dbBuilder = new EmbeddedDatabaseBuilder();
            return dbBuilder.setType(EmbeddedDatabaseType.H2)
                    .setName("persona.db")
                    .addScripts("classpath:db/schema.sql")
                    .build();
        } catch (Exception e) {
            System.out.println("DataBases didnt created");
            e.printStackTrace();
            //LOGGER.error("Встраемая база данных не создана!", e);
            return null;
        }
    }

     @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

}