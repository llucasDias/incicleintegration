package com.lucas.incicleintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthRequest(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password) {}
