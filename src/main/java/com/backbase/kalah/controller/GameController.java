package com.backbase.kalah.controller;

import com.backbase.kalah.dto.ErrorDto;
import com.backbase.kalah.dto.GameStatusDto;
import com.backbase.kalah.dto.NewGameDto;
import com.backbase.kalah.service.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Validated
public class GameController {

    private final GameService gameService;

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a new Kalah game", notes = "Creates a new Kalah game with initiated board", code = 201)
    @ApiResponses(value = @ApiResponse(code = 201, message = "Game has been created and saved", response = NewGameDto.class))
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewGameDto> createGame() {
        var newGame = gameService.newGame();

        return ResponseEntity
                .created(URI.create(newGame.getUri()))
                .body(newGame);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Make a move", notes = "Updates the game status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game has been created and saved", response = GameStatusDto.class),
            @ApiResponse(code = 400, message = "Invalid game (already finished) or pit id (e.g. pit is empty or it's a player's store)", response = ErrorDto.class),
            @ApiResponse(code = 404, message = "Game with given id wasn't found in database", response = ErrorDto.class)
    })
    @PutMapping(value = "/{gameId}/pits/{pitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameStatusDto> makeMove(@PathVariable @ApiParam(value = "Unique identifier of the game", example = "1", allowableValues = "range[1, infinity]") @Min(1) long gameId,
                                                  @PathVariable @ApiParam(value = "Id of the pit selected to make a move", example = "1", allowableValues = "range[1, 14]")
                                                  @Min(1) @Max(14) int pitId) {
        var gameStatus = gameService.move(gameId, pitId);

        return ResponseEntity.ok(gameStatus);
    }
}
