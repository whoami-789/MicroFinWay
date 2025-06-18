package com.MicroFinWay.model.enums;

import lombok.Getter;

@Getter
public enum UserType {
    INDIVIDUAL("Физическое лицо"),
    LEGAL("Юридическое лицо");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

}

