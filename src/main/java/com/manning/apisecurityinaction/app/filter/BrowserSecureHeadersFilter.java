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

@Component
@Order(FilterOrder.BROWSER_SECURE_HEADERS)
public class BrowserSecureHeadersFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(BrowserSecureHeadersFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("BrowserSecureHeadersFilter.doFilterInternal: {} {}", request.getMethod(), request.getRequestURI());

        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("Cache-Control", "max-age=0");
        response.addHeader("Expires", "0");
        response.addHeader("Content-Security-Policy", "default-src 'none'; frame-ancestors 'none'; sandbox");

        filterChain.doFilter(request, response);
    }
}
