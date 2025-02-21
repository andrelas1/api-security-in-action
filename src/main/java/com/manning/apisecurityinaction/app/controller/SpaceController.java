package com.manning.apisecurityinaction.app.controller;

import com.manning.apisecurityinaction.app.controller.dto.CreateSpaceDTO;
import com.manning.apisecurityinaction.app.controller.dto.SpaceDTO;
import com.manning.apisecurityinaction.app.core.DatabaseService;
import com.manning.apisecurityinaction.app.model.Space;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
        return databaseService.getDb().findAll(Space.class, "SELECT space_id, name, owner FROM spaces");
    }

    @PostMapping
    public ResponseEntity<CreateSpaceDTO> createSpace(@RequestBody SpaceDTO spaceDTO) {
        validateSpaceDTO(spaceDTO);

        var dbInstance = this.databaseService.getDb();

        return dbInstance.withTransaction(tx -> {
            var spaceId = dbInstance.findUniqueLong("SELECT NEXT VALUE FOR space_id_seq");

            dbInstance.updateUnique(
                    "INSERT INTO spaces(space_id, name, owner) VALUES(?, ?, ?)", spaceId, spaceDTO.name(), spaceDTO.owner()
            );

            var spaceUri = String.format("/spaces/%d", spaceId);

            return ResponseEntity
                    .created(URI.create(spaceUri))
                    .body(new CreateSpaceDTO(spaceDTO.name(), spaceUri));
        });
    }

    // TODO: find a way to validate request body in a more maintainable way
    private void validateSpaceDTO(SpaceDTO spaceDTO) {
        if (spaceDTO.name() == null || spaceDTO.name().isBlank()) {
            throw new IllegalArgumentException(String.format("the %s must not be null or blank", "name"));
        }

        if (!spaceDTO.name().matches("[a-zA-Z][a-zA-Z0-9]{1,29}")) {
            throw new IllegalArgumentException(String.format("the %s must be 2-30 characters long and start with a letter", "name"));
        }

        if (spaceDTO.owner() == null || spaceDTO.owner().isBlank()) {
            throw new IllegalArgumentException(String.format("the %s must not be null or blank", "owner"));
        }
    }
}
