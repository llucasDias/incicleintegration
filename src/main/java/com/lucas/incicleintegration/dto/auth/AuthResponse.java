package com.lucas.incicleintegration.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de resposta retornado pela API de autenticação do InCicle
 * Representa o token gerado;
 */


public record AuthResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn,
        @JsonProperty("firdt_access") boolean firstAcess) {}
