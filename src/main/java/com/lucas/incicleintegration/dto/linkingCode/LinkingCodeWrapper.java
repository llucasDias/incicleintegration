package com.lucas.incicleintegration.dto.linkingCode;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Wrapper utilizado para representar a resposta do endpoint
 * responsável por retornar os "linking codes" de colaboradores.
 *
 * O retorno da API segue este padrão:
 *
 * {
 *   "data": [
 *     { ... LinkingCodeResponse ... },
 *     { ... LinkingCodeResponse ... }
 *   ]
 * }
 *
 * Este record encapsula a lista presente dentro de "data".
 */


public record LinkingCodeWrapper(
        @JsonProperty("data")
        List<LinkingCodeResponse> data
) {}
