package com.lucas.incicleintegration.dto.invite;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Wrapper para a resposta recebida após o envio de convites
 * para colaboradores no InCicle.
 *
 * O endpoint do InCicle retorna a seguinte estrutura:
 *
 * {
 *   "collaborators": [
 *       { ... InviteResponse ... },
 *       { ... InviteResponse ... }
 *   ]
 * }
 *
 * Este record encapsula essa lista de respostas.
 */


public record InviteResponseWrapper(
        @JsonProperty("collaborators")
        List<InviteResponse> collaborators
) {}
