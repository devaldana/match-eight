package com.devspods.domain;

import lombok.*;

@Value
@Builder
public class Player {
    String firstName;
    String lastName;
    int height;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
