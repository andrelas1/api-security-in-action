package com.manning.apisecurityinaction.app.controller.dto;

import java.util.Objects;

public record CreateSpaceDTO(String name, String spaceUri) {
    public CreateSpaceDTO {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(spaceUri, "spaceUri must not be null");
    }
    
    
}
