package com.lucas.incicleintegration.service;

import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.linkingCode.LinkingCodeResponse;
import com.lucas.incicleintegration.dto.linkingCode.LinkingCodeWrapper;
import com.lucas.incicleintegration.dto.register.CollaboratorDTO;
import com.lucas.incicleintegration.dto.register.PersonResponse;
import com.lucas.incicleintegration.dto.register.RegisterRequest;
import com.lucas.incicleintegration.dto.register.UserResponse;
import com.lucas.incicleintegration.exception.AuthenticationException;
import com.lucas.incicleintegration.exception.BusinessException;
import com.lucas.incicleintegration.exception.ServerException;
import com.lucas.incicleintegration.repository.ProtheusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegisterServiceTest {

    @Mock
    private ProtheusRepository protheusRepository;

    @Mock
    private WebClient registerClient;

    @Mock
    private WebClient webClientlinkClient;

    @Mock
    private TokenService tokenService;

    @Mock
    private ApiProperties apiProperties;

    @InjectMocks
    private RegisterService registerService;

    // WebClient mocks
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;
    private WebClient.RequestHeadersUriSpec requestUriSpec;

    @BeforeEach
    void setup() {
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        requestUriSpec = mock(WebClient.RequestHeadersUriSpec.class);

        // Configurações básicas
        when(tokenService.getToken()).thenReturn("TOKEN123");
        when(apiProperties.getRegisterUrl()).thenReturn("https://fake-api.com/register");
        when(apiProperties.getCodeUrl()).thenReturn("/linking-codes");

        // Retorno padrão do repositório
        when(protheusRepository.buscarColaborador("123"))
                .thenReturn(List.of(Map.of(
                        "RA_EMAIL", "teste@empresa.com",
                        "RA_NOME", "Fulano Teste",
                        "RA_NASC", "1990-05-20"
                )));
    }

    private void mockLinkingCodeComSucesso() {
        when(webClientlinkClient.get()).thenReturn(requestUriSpec);
        when(requestUriSpec.uri(any(Function.class))).thenReturn(requestUriSpec);
        when(requestUriSpec.header(anyString(), anyString())).thenReturn(requestUriSpec);
        when(requestUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LinkingCodeWrapper.class))
                .thenReturn(Mono.just(new LinkingCodeWrapper(List.of(
                        new LinkingCodeResponse("abc123", "teste@empresa.com", "LINK-001")
                ))));
    }

    private void mockRegistroComSucesso() {
        when(registerClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(RegisterRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);

        PersonResponse person = new PersonResponse("Fulano Teste", "12345678900", "123456" , "2022-12-02", "123", "2022-02-02", "2022-03-03");
        UserResponse user = new UserResponse("fulano@email.com", "teste", "TESTE", "123", "2022-12-02", "2022-12-02");
        CollaboratorDTO collaborator = new CollaboratorDTO(person, user);

        when(responseSpec.bodyToMono(CollaboratorDTO.class)).thenReturn(Mono.just(collaborator));
    }


    @Test
    void deveLancarBusinessExceptionQuandoNaoEncontrarColaborador() {
        when(protheusRepository.buscarColaborador("999")).thenReturn(List.of());

        assertThrows(BusinessException.class,
                () -> registerService.registrarColaborador("999"));
    }

    @Test
    void deveLancarBusinessExceptionQuandoEmailInvalido() {
        when(protheusRepository.buscarColaborador("123"))
                .thenReturn(List.of(Map.of("RA_EMAIL", "", "RA_NOME", "Fulano")));

        assertThrows(BusinessException.class,
                () -> registerService.registrarColaborador("123"));
    }

    @Test
    void deveLancarAuthenticationExceptionQuando401NoLinkingCode() {
        when(webClientlinkClient.get()).thenReturn(requestUriSpec);
        when(requestUriSpec.uri(any(Function.class))).thenReturn(requestUriSpec);
        when(requestUriSpec.header(anyString(), anyString())).thenReturn(requestUriSpec);
        when(requestUriSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new AuthenticationException("Token expirado ou ausente");
        });

        assertThrows(AuthenticationException.class,
                () -> registerService.registrarColaborador("123"));
    }

    @Test
    void deveLancarBusinessExceptionQuando422NaChamada() {
        mockLinkingCodeComSucesso();

        when(registerClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new BusinessException("Problema nos dados enviados");
        });

        assertThrows(BusinessException.class,
                () -> registerService.registrarColaborador("123"));
    }


}
