package com.lucas.incicleintegration.dto.linkingCode;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LinkingCodeWrapper(
        @JsonProperty("data") List<LinkingCodeResponse> data) {
}
