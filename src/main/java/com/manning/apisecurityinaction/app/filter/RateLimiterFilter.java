package com.manning.apisecurityinaction.app.filter;


import com.manning.apisecurityinaction.app.filter.order.FilterOrder;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(FilterOrder.RATE_LIMITER)
public class RateLimiterFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);

    private static final Bucket bucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(10).refillGreedy(1, Duration.ofMinutes(1)))
            .build();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("RateLimiterFilter.doFilterInternal: {} {}", request.getMethod(), request.getRequestURI());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", "60");
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

            return;
        }

        filterChain.doFilter(request, response);
    }
}
