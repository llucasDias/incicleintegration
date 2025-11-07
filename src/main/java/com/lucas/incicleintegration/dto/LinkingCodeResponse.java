package com.lucas.incicleintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkingCodeResponse(
        @JsonProperty("id") String id,
        @JsonProperty("email") String email,
        @JsonProperty("linking_code") String linkin_code) {}
