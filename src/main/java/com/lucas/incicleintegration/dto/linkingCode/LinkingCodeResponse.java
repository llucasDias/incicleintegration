package com.lucas.incicleintegration.dto.linkingCode;

import com.fasterxml.jackson.annotation.JsonProperty;


/** DTO de envio das informações para obter o linking code **/



public record LinkingCodeResponse(
        @JsonProperty("id") String id,
        @JsonProperty("email") String email,
        @JsonProperty("linking_code") String linking_code
) {}
