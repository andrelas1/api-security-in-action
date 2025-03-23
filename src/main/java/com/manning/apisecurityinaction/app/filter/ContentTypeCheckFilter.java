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
@Order(FilterOrder.CONTENT_TYPE_CHECK)
public class ContentTypeCheckFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ContentTypeCheckFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("ContentTypeCheckFilter.doFilterInternal: {} {}", request.getMethod(), request.getRequestURI());

        if (request.getRequestURI().startsWith("/api") && request.getMethod().equals("POST")) {
            if (!request.getContentType().equals("application/json")) {
                response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
