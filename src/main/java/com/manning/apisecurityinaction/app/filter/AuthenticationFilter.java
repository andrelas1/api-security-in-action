package com.manning.apisecurityinaction.app.filter;

import com.manning.apisecurityinaction.app.core.DatabaseService;
import com.manning.apisecurityinaction.app.filter.order.FilterOrder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Order(FilterOrder.AUTHENTICATION)
public class AuthenticationFilter extends OncePerRequestFilter {
    private final DatabaseService databaseService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationFilter(DatabaseService databaseService, BCryptPasswordEncoder passwordEncoder) {
        this.databaseService = databaseService;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String USERNAME_PATTERN =
            "[a-zA-Z][a-zA-Z0-9]{1,29}";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        var offset = "Basic ".length();
        var credentials = new String(Base64.getDecoder().decode(authHeader.substring(offset)), StandardCharsets.UTF_8);

        var components = credentials.split(":", 2);
        var username = components[0];
        var password = components[1];

        if (!username.matches(USERNAME_PATTERN)) {
            throw new IllegalArgumentException("invalid username");
        }

        var hash = databaseService.getDb().findOptional(String.class,
                "SELECT pw_hash FROM users WHERE user_id = ?", username);

        if (hash.isPresent() && passwordEncoder.matches(password, hash.get())) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }


        filterChain.doFilter(request, response);
    }
}
