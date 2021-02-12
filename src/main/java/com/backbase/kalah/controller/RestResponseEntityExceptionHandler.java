package com.backbase.kalah.controller;

import com.backbase.kalah.dto.ErrorDto;
import com.backbase.kalah.exception.GameAlreadyFinishedException;
import com.backbase.kalah.exception.GameNotFoundException;
import com.backbase.kalah.exception.GameParsingException;
import com.backbase.kalah.exception.InvalidPitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GameAlreadyFinishedException.class)
    protected ResponseEntity<Object> handleGameFinished(GameAlreadyFinishedException ex, WebRequest request) {
        String errorMessage = String.format("Game '%d' is already finished", ex.getGameId());
        return handleExceptionInternal(ex, new ErrorDto(errorMessage), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(GameNotFoundException.class)
    protected ResponseEntity<Object> handleGameNotFound(GameNotFoundException ex, WebRequest request) {
        String errorMessage = String.format("Game '%d' not found", ex.getGameId());
        return handleExceptionInternal(ex, new ErrorDto(errorMessage), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidPitException.class)
    protected ResponseEntity<Object> handleInvalidPit(InvalidPitException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorDto(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(GameParsingException.class)
    protected ResponseEntity<Object> handleGameParsingFail(GameParsingException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorDto(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
