package com.lucas.incicleintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record InviteResponseWrapper(
        @JsonProperty("collaborators")List<InviteResponse> collaborators) {}
