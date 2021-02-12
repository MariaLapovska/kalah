package com.backbase.kalah.config;

import com.backbase.kalah.validation.annotation.EvenNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties("kalah")
@Validated
public class KalahProperties {

    @Min(3)
    @Max(6)
    private int stonesNum;

    @EvenNumber
    private Integer boardSize;

    private String gameLinkTemplate;
}
