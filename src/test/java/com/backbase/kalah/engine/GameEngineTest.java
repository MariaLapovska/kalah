package com.backbase.kalah.engine;

import com.backbase.kalah.config.KalahProperties;
import com.backbase.kalah.exception.GameAlreadyFinishedException;
import com.backbase.kalah.exception.InvalidPitException;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameEngineTest {
    private static final int STONES_NUM = 6;
    private static final int BOARD_SIZE = 14;

    private GameEngine instance;

    @BeforeEach
    void setup() {
        instance = new GameEngine(new KalahProperties(STONES_NUM, BOARD_SIZE, null));
    }

    @Test
    void newBoard() {
        var game = instance.newGame();
        var board = getNewBoard();

        assertThat(game.getState()).isEqualTo(GameState.PLAYER_1);
        assertThat(game.getBoard()).isEqualTo(board);
    }

    @Test
    void moveWhenEmptyCaptureThenGameStateStaysTheSameAndOppositeStonesArePutToPlayerStore() {
        var board = new HashMap<>(Map.ofEntries(
                Map.entry(1, 4), Map.entry(2, 3),
                Map.entry(3, 0), Map.entry(4, 1),
                Map.entry(5, 0), Map.entry(6, 3),
                Map.entry(7, 1),
                Map.entry(8, 5), Map.entry(9, 3),
                Map.entry(10, 2), Map.entry(11, 1),
                Map.entry(12, 2), Map.entry(13, 0),
                Map.entry(14, 0)));
        var game = new Game(1L, board, GameState.PLAYER_1);

        var updatedBoard = new HashMap<>(Map.ofEntries(
                Map.entry(1, 0), Map.entry(2, 4),
                Map.entry(3, 1), Map.entry(4, 2),
                Map.entry(5, 0), Map.entry(6, 3),
                Map.entry(7, 5),
                Map.entry(8, 5), Map.entry(9, 0),
                Map.entry(10, 2), Map.entry(11, 1),
                Map.entry(12, 2), Map.entry(13, 0),
                Map.entry(14, 0)));

        instance.move(game, 1);

        assertThat(game.getState()).isEqualTo(GameState.PLAYER_1);
        assertThat(game.getBoard()).isEqualTo(updatedBoard);
    }

    @Test
    void moveWhenPlayerLandsInStoreThenGameStateStaysTheSame() {
        var board = getNewBoard();
        var game = new Game(1L, board, GameState.PLAYER_1);

        instance.move(game, 1);

        assertThat(game.getState()).isEqualTo(GameState.PLAYER_1);
    }

    @Test
    void moveWhenPlayerDoesntLandInStoreThenGameStateChanges() {
        var board = getNewBoard();
        var game = new Game(1L, board, GameState.PLAYER_1);

        instance.move(game, 2);

        assertThat(game.getState()).isEqualTo(GameState.PLAYER_2);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForGameIsFinished")
    void moveWhenGameIsFinishedThenUpdatesBoardAndState(Map<Integer, Integer> board, Map<Integer, Integer> finalBoard,
                                                        GameState gameState, GameState finalGameState, int pitId) {
        var game = new Game(1L, board, gameState);

        instance.move(game, pitId);

        assertThat(game.getState()).isEqualTo(finalGameState);
        assertThat(game.getBoard()).isEqualTo(finalBoard);
    }

    private static Stream<Arguments> provideArgsForGameIsFinished() {
        return Stream.of(
                Arguments.of(
                        new HashMap<>(Map.ofEntries(
                                Map.entry(1, 0), Map.entry(2, 8),
                                Map.entry(3, 5), Map.entry(4, 0),
                                Map.entry(5, 0), Map.entry(6, 3),
                                Map.entry(7, 2),
                                Map.entry(8, 0), Map.entry(9, 0),
                                Map.entry(10, 0), Map.entry(11, 0),
                                Map.entry(12, 0), Map.entry(13, 1),
                                Map.entry(14, 20))),
                        new HashMap<>(Map.ofEntries(
                                Map.entry(1, 0), Map.entry(2, 0),
                                Map.entry(3, 0), Map.entry(4, 0),
                                Map.entry(5, 0), Map.entry(6, 0),
                                Map.entry(7, 18),
                                Map.entry(8, 0), Map.entry(9, 0),
                                Map.entry(10, 0), Map.entry(11, 0),
                                Map.entry(12, 0), Map.entry(13, 0),
                                Map.entry(14, 21))),
                        GameState.PLAYER_2, GameState.PLAYER_2_WON, 13),
                Arguments.of(
                        new HashMap<>(Map.ofEntries(
                                Map.entry(1, 0), Map.entry(2, 0),
                                Map.entry(3, 0), Map.entry(4, 0),
                                Map.entry(5, 0), Map.entry(6, 1),
                                Map.entry(7, 23),
                                Map.entry(8, 0), Map.entry(9, 0),
                                Map.entry(10, 0), Map.entry(11, 0),
                                Map.entry(12, 0), Map.entry(13, 1),
                                Map.entry(14, 20))),
                        new HashMap<>(Map.ofEntries(
                                Map.entry(1, 0), Map.entry(2, 0),
                                Map.entry(3, 0), Map.entry(4, 0),
                                Map.entry(5, 0), Map.entry(6, 0),
                                Map.entry(7, 24),
                                Map.entry(8, 0), Map.entry(9, 0),
                                Map.entry(10, 0), Map.entry(11, 0),
                                Map.entry(12, 0), Map.entry(13, 0),
                                Map.entry(14, 21))),
                        GameState.PLAYER_1, GameState.PLAYER_1_WON, 6),
                Arguments.of(
                        new HashMap<>(Map.ofEntries(
                                Map.entry(1, 0), Map.entry(2, 8),
                                Map.entry(3, 5), Map.entry(4, 0),
                                Map.entry(5, 0), Map.entry(6, 3),
                                Map.entry(7, 2),
                                Map.entry(8, 0), Map.entry(9, 0),
                                Map.entry(10, 0), Map.entry(11, 0),
                                Map.entry(12, 0), Map.entry(13, 1),
                                Map.entry(14, 17))),
                        new HashMap<>(Map.ofEntries(
                                Map.entry(1, 0), Map.entry(2, 0),
                                Map.entry(3, 0), Map.entry(4, 0),
                                Map.entry(5, 0), Map.entry(6, 0),
                                Map.entry(7, 18),
                                Map.entry(8, 0), Map.entry(9, 0),
                                Map.entry(10, 0), Map.entry(11, 0),
                                Map.entry(12, 0), Map.entry(13, 0),
                                Map.entry(14, 18))),
                        GameState.PLAYER_2, GameState.DRAW, 13)
        );
    }

    @ParameterizedTest
    @EnumSource(value = GameState.class, names = {"PLAYER_1_WON", "PLAYER_2_WON", "DRAW"})
    void moveWhenGameFinishedThenThrowsGameAlreadyFinishedException(GameState gameState) {
        var gameId = 1L;
        var game = new Game(gameId, Map.of(), gameState);

        assertThatThrownBy(() -> instance.move(game, 1))
                .isInstanceOf(GameAlreadyFinishedException.class)
                .hasFieldOrPropertyWithValue("gameId", gameId);
    }

    @ParameterizedTest
    @ValueSource(ints = {BOARD_SIZE / 2, BOARD_SIZE})
    void moveWhenPitIsStoreThenThrowsInvalidPitException(int pitId) {
        var game = new Game(1L, Map.of(), GameState.PLAYER_1);

        assertThatThrownBy(() -> instance.move(game, pitId))
                .isInstanceOf(InvalidPitException.class)
                .hasFieldOrPropertyWithValue("message", String.format("Invalid pit id '%d', can't be a store id", pitId));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForNotPlayerPit")
    void moveWhenPitIsNotPlayerPitThenThrowsInvalidPitException(GameState gameState, int pitId) {
        var game = new Game(1L, Map.of(), gameState);

        assertThatThrownBy(() -> instance.move(game, pitId))
                .isInstanceOf(InvalidPitException.class)
                .hasFieldOrPropertyWithValue("message", String.format("Invalid pit id '%d', can't be an opponent's pit", pitId));
    }

    private static Stream<Arguments> provideArgsForNotPlayerPit() {
        return Stream.of(
                Arguments.of(GameState.PLAYER_2, 1),
                Arguments.of(GameState.PLAYER_2, 2),
                Arguments.of(GameState.PLAYER_2, 3),
                Arguments.of(GameState.PLAYER_2, 4),
                Arguments.of(GameState.PLAYER_2, 5),
                Arguments.of(GameState.PLAYER_2, 6),
                Arguments.of(GameState.PLAYER_1, 8),
                Arguments.of(GameState.PLAYER_1, 9),
                Arguments.of(GameState.PLAYER_1, 10),
                Arguments.of(GameState.PLAYER_1, 11),
                Arguments.of(GameState.PLAYER_1, 12),
                Arguments.of(GameState.PLAYER_1, 13)
        );
    }

    @Test
    void moveWhenPitIsEmptyThenThrowsInvalidPitException() {
        var pitId = 1;
        var board = getNewBoard();
        board.put(pitId, 0);
        var game = new Game(1L, board, GameState.PLAYER_1);

        assertThatThrownBy(() -> instance.move(game, pitId))
                .isInstanceOf(InvalidPitException.class)
                .hasFieldOrPropertyWithValue("message", String.format("Pit '%d' is empty", pitId));
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