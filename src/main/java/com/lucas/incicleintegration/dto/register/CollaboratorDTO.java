package com.lucas.incicleintegration.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * DTO final da resposta de cadastro, contendo os dados de pessoa e usuário.
 *
 * Estrutura comum:
 *
 * {
 *   "person": { ... },
 *   "user": { ... }
 * }
 */

public record CollaboratorDTO (
        @JsonProperty("person") PersonResponse person,
        @JsonProperty("user") UserResponse user
){}
