package com.lucas.incicleintegration.service;


import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.auth.AuthRequest;
import com.lucas.incicleintegration.dto.auth.AuthResponse;
import com.lucas.incicleintegration.exception.AuthenticationException;
import com.lucas.incicleintegration.exception.ServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o TokenService.
 * Simula o comportamento do WebClient sem realizar chamadas reais à API.
 **/


@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    // WebClient mockado para simular requisições HTTP
    @Mock
    private WebClient authClient;

    // Propriedades da API mockadas
    @Mock
    private ApiProperties apiProperties;


    // Instância do service que será testada
    @InjectMocks
    private TokenService tokenService;

    // Mocks intermediários usados no chain do WebClient
    private WebClient.RequestBodyUriSpec requestSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;

    /**
     * Configuração antes de cada teste.
     * Cria os mocks do chain do WebClient para que cada metodo retorne o próximo mock.
     **/

    @BeforeEach
    void setup() {
        // Criar mocks do chain do WebClient
        requestSpec = mock(WebClient.RequestBodyUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);


        // Simular o chain de métodos do WebClient
        when(authClient.post()).thenReturn(requestSpec);
        when(requestSpec.bodyValue(any(AuthRequest.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }


    @Test

    /**
     * Testa cenário de sucesso ao retornar um token válido.
     **/
    void deveRetornarTokenComSucesso() {

        AuthResponse authResponse = new AuthResponse("TOKEN123", "bearer", 3600, false);
        when(responseSpec.bodyToMono(AuthResponse.class)).thenReturn(Mono.just(authResponse));

        String token = tokenService.getToken();


        assertEquals("TOKEN123", token);
        verify(authClient, times(1)).post();
    }


    /**
     * Testa cenário de erro 401, lançando AuthenticationException.
     **/

    @Test
    void deveLancarAuthenticationExceptionQuando401() {

        when(responseSpec.bodyToMono(AuthResponse.class))
                .thenReturn(Mono.error(new AuthenticationException("Erro de Autenticação")));

        assertThrows(AuthenticationException.class, () -> tokenService.getToken());
    }

    /**
     * Testa cenário de erro 500, lançando ServerException.
     **/

    @Test
    void deveLancarServerExceptionQuando500() {

        when(responseSpec.bodyToMono(AuthResponse.class))
                .thenReturn(Mono.error(new ServerException("Erro interno no servidor")));

        assertThrows(ServerException.class, () -> tokenService.getToken());
    }
}
