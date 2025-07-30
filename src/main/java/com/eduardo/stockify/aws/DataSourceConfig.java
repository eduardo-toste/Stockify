package com.eduardo.stockify.aws;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private final SecretsManagerService secretsManager;

    public DataSourceConfig(SecretsManagerService secretsManager) {
        this.secretsManager = secretsManager;
    }

    @Profile("prod")
    @Bean
    @Primary
    public DataSource awsDataSource() {
        Map<String, String> secret = secretsManager.getSecret("stockify-db");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(secret.get("url"));
        dataSource.setUsername(secret.get("username"));
        dataSource.setPassword(secret.get("password"));
        return dataSource;
    }

    @Profile("dev")
    @Bean
    @Primary
    public DataSource localDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3307/stockify");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }
}