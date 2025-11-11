package com.lucas.incicleintegration.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CollaboratorDTO (
        @JsonProperty("person") PersonResponse person,
        @JsonProperty("user") UserResponse user
){
}
