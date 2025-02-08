package com.manning.apisecurityinaction.app.model;

public class Space {
    public Long id;
    public String name;
    public String owner;

    public Space(Long id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }
}
