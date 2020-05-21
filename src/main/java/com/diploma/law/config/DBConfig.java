package com.diploma.law.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DBConfig
{

    @Value("${spring.datasource.driver-class-name}")
    private String DB_DRIVER;

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.data-username}")
    private String DB_USERNAME;

    @Value("${spring.datasource.data-password}")
    private String DB_PASSWORD;

    @Value("${spring.jpa.properties.dialect}")
    private String HIBERNATE_DIALECT;

    @Value("${spring.jpa.show-sql}")
    private String HIBERNATE_SHOW_SQL;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String HIBERNATE_DDL_AUTO;
    @Value("${spring.jpa.hibernate.use-new-id-generator-mappings}")
    private String HIBERNATE_USER_NEW_ID_GENERATOR_MAPPINGS;

    @Value("${entitymanager.packagesToScan}")
    private String ENTITY_PACKEGES_TO_SCAN;

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean localSessionFactory()
    {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan(ENTITY_PACKEGES_TO_SCAN);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("spring.jpa.properties.dialect", HIBERNATE_DIALECT);
        hibernateProperties.put("spring.jpa.show-sql", HIBERNATE_SHOW_SQL);
        hibernateProperties.put("spring.jpa.hibernate.ddl-auto", HIBERNATE_DDL_AUTO);
        hibernateProperties.put("spring.jpa.hibernate.use-new-id-generator-mappings", HIBERNATE_USER_NEW_ID_GENERATOR_MAPPINGS);
        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager()
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(localSessionFactory().getObject());
        return transactionManager;
    }
}
