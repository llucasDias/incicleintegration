package com.lucas.incicleintegration.exception;

import java.time.LocalDateTime;


/**
 * Estrutura padrão para respostas de erro enviadas pela API.
 * Utilizado em todos os handlers do GlobalExceptionHandler.
 */

public record ErrorResponse(

        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {


    /**
     * Cria uma resposta de erro preenchendo automaticamente o timestamp atual.
     */

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path);
    }

}
