package com.lucas.incicleintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record InviteRequest(
        @JsonProperty("email") String email,
        @JsonProperty("name") String name,
        @JsonProperty("level_id") String level_id,
        @JsonProperty("sector_id") String sector_id,
        @JsonProperty("unit_id") String unit_id,
        @JsonProperty("admission_date")Date admission_date,
        @JsonProperty("salary") double salary,
        @JsonProperty("employment_type") String employment_type,
        @JsonProperty("work_card_number") String work_card_number,
        @JsonProperty("cpf_number") String cpf_number,
        @JsonProperty("rg_number") String rg_number,
        @JsonProperty("rg_issuer") String rg_issuer,
        @JsonProperty("pis_number") String pis_number,
        @JsonProperty("gender") String gender
) {}
