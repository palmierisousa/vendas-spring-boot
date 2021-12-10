package io.github.palmierisousa.api.controller;

import io.github.palmierisousa.api.ApiErrors;
import io.github.palmierisousa.exception.ElementAlreadyExists;
import io.github.palmierisousa.exception.ElementNotFoundException;
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

    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleElementNotFoundException(ElementNotFoundException ex) {
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
    public ApiErrors handlePasswordInvalidException(PasswordInvalidException ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrors handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(ElementAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrors handleElementAlreadyExistsException(ElementAlreadyExists ex) {
        return new ApiErrors(ex.getMessage());
    }
}

