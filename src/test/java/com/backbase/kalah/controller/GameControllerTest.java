package com.backbase.kalah.controller;

import com.backbase.kalah.dto.ErrorDto;
import com.backbase.kalah.dto.GameStatusDto;
import com.backbase.kalah.dto.NewGameDto;
import com.backbase.kalah.exception.GameAlreadyFinishedException;
import com.backbase.kalah.exception.GameNotFoundException;
import com.backbase.kalah.exception.InvalidPitException;
import com.backbase.kalah.model.GameState;
import com.backbase.kalah.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GameControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private GameService gameService;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.gameService = mock(GameService.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new GameController(gameService), new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    void createGame() throws Exception {
        var gameUri = "www.kalah.com/game/1";
        var game = new NewGameDto(1, gameUri);

        when(gameService.newGame()).thenReturn(game);

        var result = mockMvc.perform(post("/games"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", gameUri))
                .andReturn();

        var resultGame = MAPPER.readValue(result.getResponse().getContentAsString(), NewGameDto.class);

        assertThat(resultGame).isEqualTo(game);
    }

    @Test
    void makeMove() throws Exception {
        var gameId = 1L;
        var pitId = 1;
        var game = new GameStatusDto(1, "www.kalah.com/game/1", Map.of(), GameState.PLAYER_1.getDescription());

        when(gameService.move(gameId, pitId)).thenReturn(game);

        var result = mockMvc.perform(put(String.format("/games/%d/pits/%d", gameId, pitId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var resultGame = MAPPER.readValue(result.getResponse().getContentAsString(), GameStatusDto.class);

        assertThat(resultGame).isEqualTo(game);
    }

    @Test
    void makeMoveWhenGameFinishedThenReturnsBadRequest() throws Exception {
        var gameId = 1L;
        var pitId = 1;

        when(gameService.move(gameId, pitId)).thenThrow(new GameAlreadyFinishedException(gameId));

        var result = mockMvc.perform(put(String.format("/games/%d/pits/%d", gameId, pitId)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        var resultError = MAPPER.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertThat(resultError).hasFieldOrPropertyWithValue("message", String.format("Game '%d' is already finished", gameId));
    }

    @Test
    void makeMoveWhenGameNotFoundThenReturnsNotFound() throws Exception {
        var gameId = 1L;
        var pitId = 1;

        when(gameService.move(gameId, pitId)).thenThrow(new GameNotFoundException(gameId));

        var result = mockMvc.perform(put(String.format("/games/%d/pits/%d", gameId, pitId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        var resultError = MAPPER.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertThat(resultError).hasFieldOrPropertyWithValue("message", String.format("Game '%d' not found", gameId));
    }

    @Test
    void makeMoveWhenInvalidPitIdThenReturnsBadRequest() throws Exception {
        var gameId = 1L;
        var pitId = 1;
        var errorMessage = "Invalid pit id";

        when(gameService.move(gameId, pitId)).thenThrow(new InvalidPitException(errorMessage));

        var result = mockMvc.perform(put(String.format("/games/%d/pits/%d", gameId, pitId)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        var resultError = MAPPER.readValue(result.getResponse().getContentAsString(), ErrorDto.class);

        assertThat(resultError).hasFieldOrPropertyWithValue("message", errorMessage);
    }
}