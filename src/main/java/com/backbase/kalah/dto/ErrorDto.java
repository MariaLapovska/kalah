package com.backbase.kalah.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    @ApiModelProperty(value = "Message with error description", example = "Sample error message")
    private String message;
}
