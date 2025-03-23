package com.manning.apisecurityinaction.app.config;

import com.manning.apisecurityinaction.app.controller.SpaceController;
import com.manning.apisecurityinaction.app.controller.UserController;
import com.manning.apisecurityinaction.app.core.DatabaseService;
import com.manning.apisecurityinaction.app.filter.AppFilterConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ComponentScan(basePackageClasses = {
        SpaceController.class,
        UserController.class,
        AppFilterConfiguration.class
})
public class AppConfiguration {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${database.api.username}")
    private String username;

    @Value("${database.api.password}")
    private String password;

    @Bean
    public DatabaseService databaseService() {
        return new DatabaseService(url, username, password);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
