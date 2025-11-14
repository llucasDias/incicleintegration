package com.lucas.incicleintegration.exception;

/**
 * Exceção utilizada para erros de regra de negócio.
 */


public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}