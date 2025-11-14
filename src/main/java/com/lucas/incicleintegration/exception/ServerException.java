package com.lucas.incicleintegration.exception;

/**
 * Exceção genérica utilizada para erros inesperados do servidor
 * ou respostas inesperadas da API externa.
 */

public class ServerException extends RuntimeException {

    public ServerException(String message) {
        super(message);

    }
}