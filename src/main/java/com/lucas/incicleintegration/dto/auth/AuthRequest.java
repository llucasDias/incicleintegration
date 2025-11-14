package com.lucas.incicleintegration.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO utilizado para enviar credenciais de autenticação
 * para a API do InCicle.
  */


public record AuthRequest(

        @JsonProperty("email") String email,
        @JsonProperty("password") String password
) {}
