package com.backbase.kalah.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameNotFoundException extends RuntimeException {

    private final long gameId;
}
