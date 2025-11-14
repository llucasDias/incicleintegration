package com.lucas.incicleintegration.service;

import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.auth.AuthRequest;
import com.lucas.incicleintegration.dto.auth.AuthResponse;
import com.lucas.incicleintegration.exception.AuthenticationException;
import com.lucas.incicleintegration.exception.ServerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Serviço responsável por obter token de autenticação.
 * Retorna ApiResponse<String> com sucesso/erro padronizado.
 */

@Service
public class TokenService {

    private final WebClient authClient;
    private final ApiProperties apiProperties;

    private String cachedToken;
    private Instant tokenExpiration;


    public TokenService(
            @Qualifier("authClient") WebClient authClient,
            ApiProperties apiProperties) {
        this.authClient = authClient;
        this.apiProperties = apiProperties;
    }

    public String getToken() {

        if (cachedToken != null && tokenExpiration != null && Instant.now().isBefore(tokenExpiration)) {
            return cachedToken;
        }


        AuthRequest request = new AuthRequest(apiProperties.getUsername(), apiProperties.getPassword());


            AuthResponse response = authClient.post()
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new AuthenticationException("Erro de Autenticação")))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            clientResponse -> Mono.error(new ServerException("Erro interno no servidor")))
                    .bodyToMono(AuthResponse.class)
                    .block();

            cachedToken = response.accessToken();

            tokenExpiration = Instant.now().plusSeconds(response.expiresIn());

            return cachedToken;



    }

}