package com.manning.apisecurityinaction.app.controller;

import com.manning.apisecurityinaction.app.model.Space;
import com.manning.apisecurityinaction.app.core.DatabaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    private final DatabaseService databaseService;

    public SpaceController(
            DatabaseService databaseService
    ) {
        this.databaseService = databaseService;
    }


    @GetMapping
    public List<Space> getSpaces() {
        var spaces= databaseService.getDb().findAll(Space.class, "SELECT space_id, name, owner FROM spaces");

        return spaces;
    }
}
