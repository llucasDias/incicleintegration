package com.lucas.incicleintegration.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * DTO retornado após o registro contendo informações pessoais do usuário.
 */

public record PersonResponse(
        @JsonProperty("name") String name,
        @JsonProperty("social_name") String social_name,
        @JsonProperty("user_id")String user_id,
        @JsonProperty("born_date") String born_date,
        @JsonProperty("id") String id,
        @JsonProperty("created_at") String created_at,
        @JsonProperty("updated_at") String updated_at
) {}

