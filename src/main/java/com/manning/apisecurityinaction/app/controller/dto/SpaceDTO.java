package com.manning.apisecurityinaction.app.controller.dto;

import org.springframework.validation.annotation.Validated;

@Validated
public record SpaceDTO(String name, String owner) { }
