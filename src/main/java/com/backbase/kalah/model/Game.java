package com.backbase.kalah.model;

import com.backbase.kalah.mapper.JpaConverterJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    @With
    private Long id;

    @Convert(converter = JpaConverterJson.class)
    private Map<Integer, Integer> board;

    @Enumerated(EnumType.ORDINAL)
    private GameState state;

    public Game(Map<Integer, Integer> board, GameState state) {
        this.board = board;
        this.state = state;
    }
}

