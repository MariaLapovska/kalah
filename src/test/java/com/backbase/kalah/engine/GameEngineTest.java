package com.backbase.kalah.engine;

import com.backbase.kalah.config.KalahProperties;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GameEngineTest {
    private static final int STONES_NUM = 6;
    private static final int BOARD_SIZE = 14;

    private GameEngine instance;

    @BeforeEach
    void setup() {
        instance = new GameEngine(new KalahProperties(6, 14, null));
    }

    @Test
    void newBoard() {
        var game = instance.newGame();
        var board = getNewBoard();

        assertThat(game.getState()).isEqualTo(GameState.PLAYER_1);
        assertThat(game.getBoard().size()).isEqualTo(BOARD_SIZE);
        assertThat(game.getBoard()).isEqualTo(board);
    }

    @Test
    void move() {
        var board = getNewBoard();
        var game = new Game(board, GameState.PLAYER_1);

        instance.move(game, 1);
    }

    private Map<Integer, Integer> getNewBoard() {
        return new HashMap<>(Map.ofEntries(
                Map.entry(1, STONES_NUM), Map.entry(2, STONES_NUM),
                Map.entry(3, STONES_NUM), Map.entry(4, STONES_NUM),
                Map.entry(5, STONES_NUM), Map.entry(6, STONES_NUM),
                Map.entry(7, 0),
                Map.entry(8, STONES_NUM), Map.entry(9, STONES_NUM),
                Map.entry(10, STONES_NUM), Map.entry(11, STONES_NUM),
                Map.entry(12, STONES_NUM), Map.entry(13, STONES_NUM),
                Map.entry(14, 0)));
    }
}