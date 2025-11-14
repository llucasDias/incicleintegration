package com.lucas.incicleintegration.service;


import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.invite.InviteRequestWrapper;
import com.lucas.incicleintegration.dto.invite.InviteResponseWrapper;
import com.lucas.incicleintegration.exception.*;
import com.lucas.incicleintegration.repository.ProtheusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImportServiceTest {

    @Mock
    private WebClient inviteClient;

    @Mock
    private ApiProperties apiProperties;

    @Mock
    private ProtheusRepository protheusRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private ImportService importService;

    private WebClient.RequestBodyUriSpec requestSpec;
    private WebClient.RequestHeadersSpec headersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setup() {
        requestSpec = mock(WebClient.RequestBodyUriSpec.class);
        headersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);


        when(inviteClient.post()).thenReturn(requestSpec);

        when(apiProperties.getInviteUrl()).thenReturn("https://fake-api.com/invite");
        when(requestSpec.uri("https://fake-api.com/invite")).thenReturn(requestSpec);
        when(requestSpec.header(anyString(), anyString())).thenReturn(requestSpec);
        when(requestSpec.bodyValue(any(InviteRequestWrapper.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(InviteResponseWrapper.class))
                .thenReturn(Mono.just(new InviteResponseWrapper(List.of())));

        when(tokenService.getToken()).thenReturn("TOKEN123");
        when(protheusRepository.buscarColaborador("123"))
                .thenReturn(List.of(Map.of(
                        "RA_EMAIL", "teste@empresa.com",
                        "RA_NOME", "Fulano Teste",
                        "RA_SALARIO", 5000.0,
                        "RA_SEXO", "M",
                        "RA_ADMISS", "2024-01-01"
                )));

    }


    @Test
    void deveEnviarConviteParaColaborador() {
        assertDoesNotThrow(() -> importService.importarColaboradores("123"));
    }

    @Test
    void deveLancarAuthenticationExceptionQuando401() {
        when(responseSpec.bodyToMono(InviteResponseWrapper.class))
                .thenReturn(Mono.error(new AuthenticationException("Token expirado ou ausente")));

        assertThrows(AuthenticationException.class,
                () -> importService.importarColaboradores("123"));
    }


    @Test
    void deveLancarBusinessExceptionQuando422() {
        when(responseSpec.bodyToMono(InviteResponseWrapper.class))
                .thenReturn(Mono.error(new BusinessException("Problema nos dados enviados")));

        assertThrows(BusinessException.class,
                () -> importService.importarColaboradores("123"));
    }


    @Test
    void deveLancarServerExceptionQuando500() {
        when(responseSpec.bodyToMono(InviteResponseWrapper.class))
                .thenReturn(Mono.error(new ServerException("Erro interno no servidor")));

        assertThrows(ServerException.class,
                () -> importService.importarColaboradores("123"));
    }

}
