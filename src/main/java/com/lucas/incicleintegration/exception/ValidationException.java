package com.lucas.incicleintegration.exception;

/**
 * Exceção para falhas de validação dos dados enviados.
 */


public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);

    }
}