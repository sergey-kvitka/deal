package com.kvitka.deal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class) // * 400
    public ResponseEntity<String> httpClientErrorExceptionHandler(HttpClientErrorException e) {
        String exceptionSimpleName = "HttpClientErrorException";
        String exceptionMessage = e.getMessage();
        log.warn("{} handled (message: {})", exceptionSimpleName, exceptionMessage);
        return ResponseEntity.badRequest().body(exceptionSimpleName + ": " + exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> otherExceptionHandler(Exception e) {
        String exceptionSimpleName = e.getClass().getSimpleName();
        String exceptionMessage = e.getMessage();
        log.warn("Other exception ({}) handled (message: {})", exceptionSimpleName, exceptionMessage);
        return ResponseEntity.badRequest().body(exceptionSimpleName + ": " + exceptionMessage);
    }
}
