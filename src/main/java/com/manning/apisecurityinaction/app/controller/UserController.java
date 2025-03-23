package com.manning.apisecurityinaction.app.controller;

import com.manning.apisecurityinaction.app.controller.dto.CreateUserDTO;
import com.manning.apisecurityinaction.app.controller.dto.UserDTO;
import com.manning.apisecurityinaction.app.core.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String USERNAME_PATTERN =
            "[a-zA-Z][a-zA-Z0-9]{1,29}";

    private final DatabaseService dbService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(DatabaseService dbService, BCryptPasswordEncoder passwordEncoder) {
        this.dbService = dbService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public UserDTO registerUser(@RequestBody CreateUserDTO createUserDTO) {
        var username = createUserDTO.username();
        var password = createUserDTO.password();

        if (!username.matches(USERNAME_PATTERN)) {
            throw new IllegalArgumentException("invalid username");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "password must be at least 8 characters");
        }

        var hash = passwordEncoder.encode(password);
        dbService.getDb().updateUnique(
                "INSERT INTO users(user_id, pw_hash)" +
                        " VALUES(?, ?)", username, hash);

//        response.status(201);
//        response.header("Location", "/users/" + username);
//        return new JSONObject().put("username", username);
        logger.info("User registered: {} with password: {}", username, hash);
        return new UserDTO(username);
    }
}
