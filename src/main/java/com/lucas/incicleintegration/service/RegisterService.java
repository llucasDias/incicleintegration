package com.lucas.incicleintegration.service;


import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.register.CollaboratorDTO;
import com.lucas.incicleintegration.dto.register.RegisterRequest;
import com.lucas.incicleintegration.exception.AuthenticationException;
import com.lucas.incicleintegration.exception.BusinessException;
import com.lucas.incicleintegration.exception.ServerException;
import com.lucas.incicleintegration.repository.ProtheusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static com.lucas.incicleintegration.util.ValidarDados.formatarData;

@Service
public class RegisterService {

    private final ProtheusRepository protheusRepository;
    private final WebClient registerClient;
    private final TokenService tokenService;
    private final LinkingCodeService linkingCodeService;
    private final ApiProperties apiProperties;

    public RegisterService(
            ProtheusRepository protheusRepository,
            @Qualifier("registerClient") WebClient registerClient, TokenService tokenService, LinkingCodeService linkingCodeService, ApiProperties apiProperties) {
        this.protheusRepository = protheusRepository;
        this.registerClient = registerClient;
        this.tokenService = tokenService;
        this.linkingCodeService = linkingCodeService;
        this.apiProperties = apiProperties;
    }

    public void registrarColaborador(String matricula) {

        List<Map<String, Object>> resultado = protheusRepository.buscarColaborador(matricula);

        String token = tokenService.getToken();
        String codigo = linkingCodeService.getUltimoLinkingCode();

        RegisterRequest request = registroColaborador(resultado.get(0), codigo);


        CollaboratorDTO  response = registerClient.post()
                .uri(apiProperties.getRegisterUrl())
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
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
                .bodyToMono(CollaboratorDTO.class)
                .block();

    }

    private RegisterRequest registroColaborador(Map<String, Object> c, String codigo) {
        String dataFormatada = formatarData(c.get("RA_NASC"));


        return new RegisterRequest(
                (String) c.get("RA_NOME"),
                (String) c.get("RA_EMAIL"),
                (String) c.get("RA_EMAIL"),
                "caf102030",
                "caf102030",
                dataFormatada,
                null,
                "America/Sao_Paulo",
                codigo,
                true

        );
    }
}
