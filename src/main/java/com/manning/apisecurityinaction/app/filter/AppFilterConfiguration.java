package com.manning.apisecurityinaction.app.filter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        RequestLoggerFilter.class,
        BrowserSecureHeadersFilter.class,
        ContentTypeCheckFilter.class
})
public class AppFilterConfiguration {
}
