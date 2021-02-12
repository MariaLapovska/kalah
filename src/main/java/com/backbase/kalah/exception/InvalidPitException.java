package com.backbase.kalah.exception;

import lombok.Getter;

@Getter
public class InvalidPitException extends RuntimeException {

    public InvalidPitException(String message) {
        super(message);
    }
}
