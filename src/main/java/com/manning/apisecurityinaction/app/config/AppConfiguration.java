package com.manning.apisecurityinaction.app.config;

import com.manning.apisecurityinaction.app.controller.SpaceController;
import com.manning.apisecurityinaction.app.core.DatabaseService;
import com.manning.apisecurityinaction.app.filter.AppFilterConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        SpaceController.class,
        AppFilterConfiguration.class
})
public class AppConfiguration {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DatabaseService databaseService() {
        return new DatabaseService(url, username, password);
    }
}
