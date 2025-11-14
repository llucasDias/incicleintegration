package com.lucas.incicleintegration.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO retornado após o registro contendo informações pessoais do usuário.
 */

public record UserResponse(
        @JsonProperty("email") String email,
        @JsonProperty("username") String username,
        @JsonProperty("type") String type,
        @JsonProperty("id") String id,
        @JsonProperty("created_at") String created_at,
        @JsonProperty("updated_at") String updated_at

) {}


