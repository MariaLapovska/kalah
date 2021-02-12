package com.backbase.kalah.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGameDto {
    @ApiModelProperty(value = "Unique identifier of the newly created game", example = "1")
    private long id;

    @ApiModelProperty(value = "Link to the newly created game", example = "http://localhost:8080/games/1")
    private String uri;
}
