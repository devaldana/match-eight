package com.devspods.domain;

import lombok.Value;

@Value
public class Pair {
    Player player1;
    Player player2;

    public String toString() {
        return String.format("%-30.30s  %-30.30s%n", getDescription(player1), getDescription(player2));
    }

    private String getDescription(final Player player) {
        return String.format("%s\" - %s", player.getHeight(), player.getFullName());
    }
}
