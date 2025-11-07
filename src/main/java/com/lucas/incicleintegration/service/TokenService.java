package com.lucas.incicleintegration.service;

import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.AuthRequest;
import com.lucas.incicleintegration.dto.AuthResponse;
import com.lucas.incicleintegration.exception.AuthenticationException;
import com.lucas.incicleintegration.exception.ServerException;
import com.lucas.incicleintegration.exception.WebClientResponseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Serviço responsável por obter token de autenticação.
 * Retorna ApiResponse<String> com sucesso/erro padronizado.
 */

@Service
public class TokenService {

    private final WebClient authClient;
    private final ApiProperties apiProperties;

    public TokenService(
            @Qualifier("authClient") WebClient authClient,
            ApiProperties apiProperties) {
        this.authClient = authClient;
        this.apiProperties = apiProperties;
    }

    public String getToken() {

        AuthRequest request = new AuthRequest(apiProperties.getUsername(), apiProperties.getPassword());

        try {
            AuthResponse response = authClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new AuthenticationException("Erro de Autenticação")))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            clientResponse -> Mono.error(new ServerException("Erro interno no servidor")))
                    .bodyToMono(AuthResponse.class)
                    .block();

            return response.accessToken();

        } catch (AuthenticationException | ServerException e) {
            throw e;
        } catch (Exception e) {
            throw new WebClientResponseException("Erro inesperado" + e.getMessage());
        }

    }

}