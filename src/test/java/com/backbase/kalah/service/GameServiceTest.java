package com.backbase.kalah.service;

import com.backbase.kalah.dto.GameStatusDto;
import com.backbase.kalah.dto.NewGameDto;
import com.backbase.kalah.engine.GameEngine;
import com.backbase.kalah.exception.GameNotFoundException;
import com.backbase.kalah.mapper.GameMapper;
import com.backbase.kalah.model.Game;
import com.backbase.kalah.model.GameState;
import com.backbase.kalah.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameEngine gameEngine;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameService instance;

    @Test
    void newGame() {
        var gameId = 1L;
        var gameUri = "www.kalah.com/game/1";
        var game = new Game(null, Map.of(), GameState.PLAYER_1);
        var gameDto = new NewGameDto(gameId, gameUri);

        when(gameEngine.newGame()).thenReturn(game);
        when(gameRepository.save(game)).thenReturn(game.withId(gameId));
        when(gameMapper.gameToDto(game.withId(gameId))).thenReturn(gameDto);

        assertThat(instance.newGame()).isEqualTo(gameDto);
    }

    @Test
    void move() {
        var gameId = 1L;
        var pitId = 1;
        var gameUri = "www.kalah.com/game/1";
        var game = new Game(gameId, Map.of(), GameState.PLAYER_1);
        var gameDto = new GameStatusDto(gameId, gameUri, Map.of(), GameState.PLAYER_1.getDescription());

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.gameToStatusDto(game)).thenReturn(gameDto);

        assertThat(instance.move(gameId, pitId)).isEqualTo(gameDto);

        verify(gameEngine).move(game, pitId);
    }

    @Test
    void moveWhenGameNotFoundThenThrowsGameNotFoundException() {
        var gameId = 1L;
        var pitId = 1;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instance.move(gameId, pitId))
                .isInstanceOf(GameNotFoundException.class)
                .hasFieldOrPropertyWithValue("gameId", gameId);
    }
}