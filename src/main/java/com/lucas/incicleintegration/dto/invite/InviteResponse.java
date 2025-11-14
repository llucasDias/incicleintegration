package com.lucas.incicleintegration.dto.invite;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * DTO de resposta do envio do Convite
 */

public record
InviteResponse(
        @JsonProperty("id") String id,
        @JsonProperty("email") String email,
        @JsonProperty("name") String name) {}
