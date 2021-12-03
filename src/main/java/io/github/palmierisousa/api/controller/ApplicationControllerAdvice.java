package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.ApiErrors;
import io.github.palmierisousa.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleRegraNegocioException(NotFoundException ex) {
        return new ApiErrors(ex.getMessage());
    }
}

