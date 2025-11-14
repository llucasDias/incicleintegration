package com.lucas.incicleintegration.exception;

/**
 * Exceção lançada quando ocorre erro de autenticação
 * na comunicação com a API externa.
 */


public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
