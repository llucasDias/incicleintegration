package com.lucas.incicleintegration.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthRequest(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password) {}
