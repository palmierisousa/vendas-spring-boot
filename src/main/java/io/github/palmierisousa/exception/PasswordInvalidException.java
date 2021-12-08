package io.github.palmierisousa.exception;

public class PasswordInvalidException extends RuntimeException {
    public PasswordInvalidException() {
        super("Senha inválida");
    }
}
