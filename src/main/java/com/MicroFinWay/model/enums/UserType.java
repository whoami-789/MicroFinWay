package com.MicroFinWay.model.enums;

public enum UserType {
    INDIVIDUAL("Физическое лицо"),
    LEGAL("Юридическое лицо");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

