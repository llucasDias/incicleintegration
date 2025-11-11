package com.lucas.incicleintegration.dto.invite;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record InviteRequestWrapper(
        @JsonProperty("collaborators")List<InviteRequest> collaboratos) {}
