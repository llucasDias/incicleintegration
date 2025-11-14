package com.lucas.incicleintegration.dto.invite;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Wrapper utilizado pela API do InCicle para receber uma lista
 * de colaboradores que serão convidados.
 *
 * O endpoint de convite exige que o payload seja no seguinte formato:
 *
 * {
 *   "collaborators": [
 *       { ... InviteRequest ... },
 *       { ... InviteRequest ... }
 *   ]
 * }
 *
 * Este record encapsula essa estrutura.
 */
public record InviteRequestWrapper(
        @JsonProperty("collaborators")List<InviteRequest> collaborators) {}
