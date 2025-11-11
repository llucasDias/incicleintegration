package com.lucas.incicleintegration.service;


import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.linkingCode.LinkingCodeResponse;
import com.lucas.incicleintegration.exception.AuthenticationException;
import com.lucas.incicleintegration.exception.BusinessException;
import com.lucas.incicleintegration.exception.ServerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LinkingCodeService {

    private final WebClient webClientlinkClient;
    private final TokenService tokenService;
    private final ApiProperties apiProperties;

    private String ultimoLinkingCode;


    public LinkingCodeService(@Qualifier("linkClient")WebClient webClientlinkClient,
                              TokenService tokenService, ApiProperties apiProperties) {
        this.webClientlinkClient = webClientlinkClient;
        this.tokenService = tokenService;
        this.apiProperties = apiProperties;
    }

    public LinkingCodeResponse buscarLinkingCode(String email) {
        String token = tokenService.getToken();

        LinkingCodeResponse response = webClientlinkClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiProperties.getCodeUrl())
                        .queryParam("email", email)
                        .build())
                .header("Authorization", "Bearer " + token)
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
                .bodyToMono(LinkingCodeResponse.class)
                .block();

        String linkingCode = response.linking_code();

        salvarLinkingcode(linkingCode);

        return response;
    }

    public void salvarLinkingcode(String linkingCode) {
        this.ultimoLinkingCode = linkingCode;

    }

    public String getUltimoLinkingCode() {
        return this.ultimoLinkingCode;
    }

}
