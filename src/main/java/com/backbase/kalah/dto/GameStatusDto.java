package com.backbase.kalah.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStatusDto {

    @ApiModelProperty(value = "Unique identifier of the game", example = "1")
    private long id;

    @ApiModelProperty(value = "Link to the game", example = "http://localhost:8080/games/1")
    private String uri;

    @ApiModelProperty(value = "Json key-value object, where key is the pitId and value is the number of stones in the pit",
            example = "{'1':'4','2':'4','3':'4','4':'4','5':'4','6':'4','7':'0','8':'4','9':'4','10':'4','11':'4','12':'4','13':'4','14':'0'}")
    private Map<Integer, Integer> status;

    @ApiModelProperty(value = "Indicator of the game state, hints on which player should make the next move", example = "Player 1 turn")
    private String gameState;
}