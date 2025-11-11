package com.lucas.incicleintegration.dto.linkingCode;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkingCodeResponse(
        @JsonProperty("id") String id,
        @JsonProperty("email") String email,
        @JsonProperty("linking_code") String linking_code) {}
