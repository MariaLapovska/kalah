package com.backbase.kalah.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameState {
    PLAYER_1("Player 1 turn"), PLAYER_2("Player 2 turn"), PLAYER_1_WON("Player 1 won"), PLAYER_2_WON("Player 2 won"), DRAW("Draw");

    private final String description;
}
