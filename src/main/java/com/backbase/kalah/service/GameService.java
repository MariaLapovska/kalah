package com.backbase.kalah.service;

import com.backbase.kalah.dto.GameStatusDto;
import com.backbase.kalah.dto.NewGameDto;
import com.backbase.kalah.engine.GameEngine;
import com.backbase.kalah.exception.GameNotFoundException;
import com.backbase.kalah.mapper.GameMapper;
import com.backbase.kalah.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameEngine gameEngine;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public NewGameDto newGame() {
        return gameMapper.gameToDto(gameRepository.save(gameEngine.newGame()));
    }

    @Transactional
    public GameStatusDto move(long gameId, int pitId) {
        var game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        gameEngine.move(game, pitId);
        return gameMapper.gameToStatusDto(gameRepository.save(game));
    }
}
