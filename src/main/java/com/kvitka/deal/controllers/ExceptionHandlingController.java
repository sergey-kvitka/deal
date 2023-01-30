package com.kvitka.deal.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class) // * 400
    public ResponseEntity<String> httpClientErrorExceptionHandler(HttpClientErrorException e) {
        String exceptionSimpleName = "HttpClientErrorException";
        String exceptionMessage = e.getMessage();
        Matcher matcher = Pattern.compile("\\[(.*?)]").matcher(exceptionMessage == null ? "" : exceptionMessage);
        log.warn("{} handled (message: {})", exceptionSimpleName, exceptionMessage);
        return ResponseEntity.badRequest().body(matcher.find()
                ? matcher.group(1)
                : exceptionSimpleName + ": " + exceptionMessage
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceAccessException.class) // * connection refused
    public ResponseEntity<String> resourceAccessExceptionHandler(ResourceAccessException e) {
        String exceptionSimpleName = "ResourceAccessException";
        String exceptionMessage = e.getMessage();
        Matcher matcher = Pattern.compile("\"(.*?)\"").matcher(exceptionMessage == null ? "" : exceptionMessage);
        if (matcher.find()) {
            exceptionMessage = String.format("connection refused (%s)", matcher.group(1));
        }
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
