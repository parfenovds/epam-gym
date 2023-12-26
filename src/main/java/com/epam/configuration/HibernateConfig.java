package com.epam.configuration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:application.properties")
public class HibernateConfig {

  @Value("${db.url}")
  private String URL;
  @Value("${db.username}")
  private String USERNAME;
  @Value("${db.password}")
  private String PASSWORD;
  @Value("${db.show-sql}")
  private String SHOW_SQL;
  @Value("${db.dialect}")
  private String DIALECT;
  @Value("${db.hbm2ddl-auto}")
  private String HBM2DDL_AUTO;

  @Bean
  public DataSource dataSource() {
    return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
  }

  @Bean
  public LocalSessionFactoryBean localSessionFactoryBean(DataSource dataSource) {
    LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
    localSessionFactoryBean.setDataSource(dataSource);
    localSessionFactoryBean.setPackagesToScan("com.epam.entity");

    Properties properties = new Properties();
    properties.put(Environment.DIALECT, DIALECT);
    properties.put(Environment.SHOW_SQL, SHOW_SQL);
    properties.put(Environment.HBM2DDL_AUTO, HBM2DDL_AUTO);
    localSessionFactoryBean.setHibernateProperties(properties);

    return localSessionFactoryBean;
  }

  @Bean
  public SessionFactory sessionFactory(LocalSessionFactoryBean localSessionFactoryBean) {
    return localSessionFactoryBean.getObject();
  }

  @Bean
  public HibernateTransactionManager platformTransactionManager(SessionFactory sessionFactory) {
    return new HibernateTransactionManager(sessionFactory);
  }

  @Bean
  public Validator validator() {
    ValidatorFactory validatorFactory = Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(new ParameterMessageInterpolator())
        .buildValidatorFactory();

    return validatorFactory.getValidator();
  }
}
