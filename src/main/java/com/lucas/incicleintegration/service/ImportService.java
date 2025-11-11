package com.lucas.incicleintegration.service;


import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.invite.InviteRequest;
import com.lucas.incicleintegration.dto.invite.InviteRequestWrapper;
import com.lucas.incicleintegration.dto.invite.InviteResponseWrapper;
import com.lucas.incicleintegration.dto.linkingCode.LinkingCodeResponse;
import com.lucas.incicleintegration.exception.AuthenticationException;
import com.lucas.incicleintegration.exception.BusinessException;
import com.lucas.incicleintegration.exception.ServerException;
import com.lucas.incicleintegration.exception.ValidationException;
import com.lucas.incicleintegration.repository.ProtheusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.lucas.incicleintegration.util.ValidarDados.*;

@Service
public class ImportService {

    private final ProtheusRepository protheusRepository;
    private final WebClient inviteClient;
    private final ApiProperties apiProperties;
    private final TokenService tokenService;
    private final LinkingCodeService linkingCodeService;

    public ImportService(
            ProtheusRepository protheusRepository,
            @Qualifier("inviteClient") WebClient inviteClient,
            ApiProperties apiProperties,
            TokenService tokenService, LinkingCodeService linkingCodeService
    ) {
        this.protheusRepository = protheusRepository;
        this.inviteClient = inviteClient;
        this.apiProperties = apiProperties;
        this.tokenService = tokenService;
        this.linkingCodeService = linkingCodeService;
    }

    /**
     * Metodo principal que importa colaboradores do Protheus e envia convite via API externa.
     * @param matricula código do colaborador para busca no Protheus
     **/

    public void importarColaboradores(String matricula) {

        // Buscar colaboradores no Protheus pelo RA (matricula)
        List<Map<String, Object>> resultado = protheusRepository.buscarColaborador(matricula);
        if (resultado.isEmpty()) {
            throw new ValidationException("Nenhum colaborador encontrado");
        }

        // Obter token de autenticação via TokenService
        String token = tokenService.getToken();

        // Mapear os dados do Protheus para DTO de envio (InviteRequest)
        List<InviteRequest> listaInvite = resultado.stream()
                .map(this::mapearColaborador)
                .toList();

        InviteRequestWrapper wrapper = new InviteRequestWrapper(listaInvite);

        // Enviar POST para API com tratamento de erros HTTP
        InviteResponseWrapper responseWrapper = inviteClient.post()
                .uri(apiProperties.getInviteUrl())
                .header("Authorization", "Bearer " + token) // Adiciona token
                .bodyValue(wrapper)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    switch (clientResponse.statusCode().value()) {
                        case 401:
                            return Mono.error(new AuthenticationException("Token expirado ou ausente"));
                        case 422:
                            return Mono.error(new BusinessException("Problema nos dados enviados"));
                        default:
                            return Mono.error(new BusinessException("Erro 4xx desconhecido"));
                    }
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ServerException("Erro interno no servidor")))
                .bodyToMono(InviteResponseWrapper.class)
                .block();


        if (responseWrapper != null && responseWrapper.collaborators() != null) {
            String email = responseWrapper.collaborators().get(0).email();

            LinkingCodeResponse linkingCodeResponse = linkingCodeService.buscarLinkingCode(email);
        }
    }

    /**
     * Mapeia os dados do Protheus para o DTO InviteRequest, aplicando validações
     **/
    private InviteRequest mapearColaborador(Map<String, Object> c) {
        double salario = validarSalario(c.get("RA_SALARIO"));
        String sexo = validarSexo((String) c.get("RA_SEXO"));
        String dataFormatada = formatarData(c.get("RA_ADMISS"));

        return new InviteRequest(
                (String) c.get("RA_EMAIL"),
                (String) c.get("RA_NOME"),
                null,
                null,
                null,
                dataFormatada,
                salario,
                "FULL_TIME",
                (String) c.get("RA_NUMCP"),
                (String) c.get("RA_CIC"),
                (String) c.get("RA_RG"),
                (String) c.get("RA_ORGEMRG"),
                (String) c.get("RA_PIS"),
                sexo
        );
    }
}

