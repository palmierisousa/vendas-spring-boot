package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.ApiErrors;
import io.github.palmierisousa.exception.NotFoundException;
import io.github.palmierisousa.exception.PasswordInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleNotFoundException(NotFoundException ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage()).collect(Collectors.toList());

        return new ApiErrors(erros);
    }

    @ExceptionHandler(PasswordInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrors handleNotFoundException(PasswordInvalidException ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrors handleNotFoundException(UsernameNotFoundException ex) {
        return new ApiErrors(ex.getMessage());
    }
}

