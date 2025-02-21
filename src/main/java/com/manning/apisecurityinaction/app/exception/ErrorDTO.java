package com.manning.apisecurityinaction.app.exception;

import java.util.Objects;

public record ErrorDTO(int status, String error, String message) {
    public ErrorDTO {
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(error, "error must not be null");
        Objects.requireNonNull(message, "message must not be null");
    }
}
