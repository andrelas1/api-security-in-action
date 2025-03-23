package com.manning.apisecurityinaction.app.filter;

import com.manning.apisecurityinaction.app.filter.order.FilterOrder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// more context: https://www.baeldung.com/spring-mvc-handlerinterceptor-vs-filter
@Component
@Order(FilterOrder.REQUEST_LOGGER)
public class RequestLoggerFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggerFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        logger.info("RequestLoggerFilter.doFilterInternal: {} {}", request.getMethod(), request.getRequestURI());

        response.addHeader("X-Request-Id", UUID.randomUUID().toString());

        filterChain.doFilter(request, response);

        logger.info("Responded with status: {}", response.getStatus());
    }
}
