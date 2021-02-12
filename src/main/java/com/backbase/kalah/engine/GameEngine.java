package com.backbase.kalah.engine;

import com.backbase.kalah.config.KalahProperties;
import com.backbase.kalah.exception.GameAlreadyFinishedException;
import com.backbase.kalah.exception.InvalidPitException;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.GameState;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class implements Kalah game logic. It is responsible for player moves processing and initializing the board - filling
 * all pits with stones in the beginning of the game.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Kalah">Kalah rules</a>
 */
@Log4j2
@Component
public class GameEngine {

    /**
     * Number of pits on the Kalah board
     */
    private final int boardSize;

    private final int halfBoardSize;

    /**
     * Number of stones in each pit in the beginning of the game (except players stores)
     */
    private final int stonesNum;

    public GameEngine(KalahProperties kalahProperties) {
        this.boardSize = kalahProperties.getBoardSize();
        this.halfBoardSize = boardSize / 2;
        this.stonesNum = kalahProperties.getStonesNum();
    }

    /**
     * Initialize a new Kalah game of the {@link #boardSize}, where each pit except players stores contains {@link #stonesNum}
     *
     * @return new Kalah game
     */
    public Game newGame() {
        var board = new HashMap<Integer, Integer>();

        for (int pitId = 1; pitId <= boardSize; pitId++) {
            int stonesNum = isStore(pitId) ? 0 : this.stonesNum;
            board.put(pitId, stonesNum);
        }

        return new Game(board, GameState.PLAYER_1);
    }

    /**
     * Make a move and update the Kalah game accordingly
     *
     * @param game  Kalah game
     * @param pitId id of the pit selected to make a move
     * @throws GameAlreadyFinishedException if game has already been finished
     * @throws InvalidPitException          if pit by given id is empty or is a player's store
     */
    public void move(Game game, int pitId) {
        var gameState = game.getState();

        validateGameState(game.getId(), gameState);
        validatePit(gameState, pitId);

        var board = game.getBoard();

        int stonesNum = Optional.ofNullable(board.put(pitId, 0)).orElse(0);
        if (stonesNum == 0) {
            throw new InvalidPitException(String.format("Pit '%d' is empty", pitId));
        }

        int playerStorePitId = gameState == GameState.PLAYER_1 ? halfBoardSize : boardSize;
        int opponentStorePitId = gameState == GameState.PLAYER_1 ? boardSize : halfBoardSize;

        log.debug("Moving '{}' stones from pit '{}' for game '{}'", stonesNum, game.getId(), pitId);

        int i = incrementPitId(pitId, opponentStorePitId);
        while (stonesNum > 1) {
            board.computeIfPresent(i, (key, oldStonesNum) -> oldStonesNum + 1);
            stonesNum--;
            i = incrementPitId(i, opponentStorePitId);
        }

        if (isPlayerPit(gameState, i) && board.get(i) == 0) {
            int oppositePitId = boardSize - i;
            int oppositeStonesNum = Optional.ofNullable(board.put(oppositePitId, 0)).orElse(0);
            board.computeIfPresent(playerStorePitId, (key, stonesInStoreNum) -> stonesInStoreNum + oppositeStonesNum + 1);
        } else {
            board.computeIfPresent(i, (key, oldStonesNum) -> oldStonesNum + 1);

            if (i != playerStorePitId) {
                game.setState(gameState == GameState.PLAYER_1 ? GameState.PLAYER_2 : GameState.PLAYER_1);
            }
        }

        checkGameEnd(board).ifPresent(state -> {
            game.setState(state);
            log.debug("Game '{}' has ended, result: {}", game.getId(), state.getDescription());
        });
    }

    private boolean isStore(int pitId) {
        return pitId == halfBoardSize || pitId == boardSize;
    }

    private void validateGameState(long gameId, GameState state) {
        if (state == GameState.PLAYER_1_WON
                || state == GameState.PLAYER_2_WON
                || state == GameState.DRAW) {
            throw new GameAlreadyFinishedException(gameId);
        }
    }

    private void validatePit(GameState state, int pitId) {
        if (isStore(pitId)) {
            throw new InvalidPitException(String.format("Invalid pit id '%d', can't be a store id", pitId));
        }
        if (!isPlayerPit(state, pitId)) {
            throw new InvalidPitException(String.format("Invalid pit id '%d', can't be an opponent's pit", pitId));
        }
    }

    private boolean isPlayerPit(GameState state, int pitId) {
        if (state == GameState.PLAYER_1) {
            return pitId < halfBoardSize;
        } else {
            return pitId > halfBoardSize && pitId < boardSize;
        }
    }

    private int incrementPitId(int pitId, int opponentStorePitId) {
        pitId = pitId + 1 == opponentStorePitId ? pitId + 2 : pitId + 1;
        return pitId > boardSize ? pitId % boardSize : pitId;
    }

    private Optional<GameState> checkGameEnd(Map<Integer, Integer> board) {
        if (allPitsAreEmpty(board, 1, halfBoardSize) || allPitsAreEmpty(board, halfBoardSize + 1, boardSize)) {
            int player1Stones = transferStones(board, 1, halfBoardSize);
            int player2Stones = transferStones(board, halfBoardSize + 1, boardSize);
            GameState gameState;

            if (player1Stones > player2Stones) {
                gameState = GameState.PLAYER_1_WON;
            } else if (player2Stones > player1Stones) {
                gameState = GameState.PLAYER_2_WON;
            } else {
                gameState = GameState.DRAW;
            }

            return Optional.of(gameState);
        }

        return Optional.empty();
    }

    private boolean allPitsAreEmpty(Map<Integer, Integer> board, int pitId, int storeId) {
        while (pitId < storeId && board.get(pitId) == 0) {
            pitId++;
        }

        return pitId == storeId;
    }

    private int transferStones(Map<Integer, Integer> board, int pitId, int storeId) {
        int totalStonesNum = 0;

        while (pitId < storeId) {
            totalStonesNum += board.get(pitId);
            pitId++;
        }

        int finalTotalStonesNum = totalStonesNum;
        return board.computeIfPresent(storeId, (key, oldValue) -> oldValue + finalTotalStonesNum);
    }
}
