package com.lucas.incicleintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record RegisterRequest(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("email_confirmation") String email_confirmation,
        @JsonProperty("password") String password,
        @JsonProperty("password_confirmation") String password_confirmation,
        @JsonProperty("born")Date born,
        @JsonProperty("phone") String phone,
        @JsonProperty("timezone") String timezone,
        @JsonProperty("linking_code") String linking_code,
        @JsonProperty("accepted") boolean accepted) {}
