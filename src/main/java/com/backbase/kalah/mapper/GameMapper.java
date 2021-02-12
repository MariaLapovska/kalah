package com.backbase.kalah.mapper;

import com.backbase.kalah.config.KalahProperties;
import com.backbase.kalah.dto.GameStatusDto;
import com.backbase.kalah.dto.NewGameDto;
import com.backbase.kalah.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    private final String linkTemplate;

    public GameMapper(KalahProperties kalahProperties) {
        this.linkTemplate = kalahProperties.getGameLinkTemplate();
    }

    public NewGameDto gameToDto(Game game) {
        return new NewGameDto(game.getId(), generateGameLink(game.getId()));
    }

    public GameStatusDto gameToStatusDto(Game game) {
        return new GameStatusDto(game.getId(), generateGameLink(game.getId()), game.getBoard(), game.getState().getDescription());
    }

    private String generateGameLink(long gameId) {
        return linkTemplate + "/" + gameId;
    }
}
