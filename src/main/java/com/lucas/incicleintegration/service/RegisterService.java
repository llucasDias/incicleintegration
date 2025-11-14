package com.lucas.incicleintegration.service;


import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.linkingCode.LinkingCodeResponse;
import com.lucas.incicleintegration.dto.linkingCode.LinkingCodeWrapper;
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
    private final WebClient webClientlinkClient;
    private final TokenService tokenService;
    private final ApiProperties apiProperties;

    public RegisterService(
            ProtheusRepository protheusRepository,
            @Qualifier("registerClient") WebClient registerClient,
            @Qualifier("linkClient") WebClient webClientlinkClient,
            TokenService tokenService,
            ApiProperties apiProperties) {
        this.protheusRepository = protheusRepository;
        this.registerClient = registerClient;
        this.webClientlinkClient = webClientlinkClient;
        this.tokenService = tokenService;
        this.apiProperties = apiProperties;
    }


    /**
     * Registra colaborador no sistema externo, recuperando o linking code via e-mail.
     *
     * Fluxo:
     *  1. Busca colaborador no Protheus.
     *  2. Obtém token.
     *  3. Busca linking code via e-mail.
     *  4. Monta payload de registro.
     *  5. Envia solicitação POST para API.
     *
     * Exceções aplicadas:
     *  - BusinessException → dados inválidos do Protheus ou do fluxo.
     *  - AuthenticationException → token expirado/ausente.
     *  - ServerException → erro interno 5xx da API externa.
     */


    public void registrarColaborador(String matricula) {

        List<Map<String, Object>> resultado = protheusRepository.buscarColaborador(matricula);

        if (resultado.isEmpty()) {
            throw new BusinessException("Nenhum colaborador encontrado para matrícula " + matricula);
        }

        Map<String, Object> colaborador = resultado.get(0);
        String email = trim(colaborador.get("RA_EMAIL"));

        if (email == null || email.isEmpty()) {
            throw new BusinessException("E-mail do colaborador não encontrado.");
        }

        // Obter Token
        String token = tokenService.getToken();

        // Recuperar linking code
        String codigo = recuperarLinkingCode(email, token);


        //Montar payload
        RegisterRequest request = registroColaborador(colaborador, codigo);


        // 🚀 5. Enviar POST
        CollaboratorDTO response = registerClient.post()
                .uri(apiProperties.getRegisterUrl())
                .header("Authorization", "Bearer " + token)
                .header("companyId", "750a4df3-28ca-41f0-a02a-ddc502dddefb")
                .bodyValue(request)
                .retrieve()

                // Tratamento de Exceções
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(body -> {
                                    int status = clientResponse.statusCode().value();
                                    switch (status) {
                                        case 401:
                                            return Mono.error(new AuthenticationException("Token expirado ou ausente: " + body));
                                        case 422:
                                            return Mono.error(new BusinessException("Problema nos dados enviados: " + body));
                                        default:
                                            return Mono.error(new BusinessException("Erro 4xx desconhecido: " + body));
                                    }
                                }))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ServerException("Erro interno no servidor")))


                // Desserializa a resposta
                .bodyToMono(CollaboratorDTO.class)
                .block();
    }


    /**
     * Monta a requisição para registro do colaborador na API externa.
     */

    private RegisterRequest registroColaborador(Map<String, Object> c, String codigo) {

        // Formata a data para o padrão da API
        String dataFormatada = formatarData((String) c.get("RA_NASC"));

        return new RegisterRequest(
                trim(c.get("RA_NOME")),
                trim(c.get("RA_EMAIL")),
                trim(c.get("RA_EMAIL")),
                "caf102030",
                "caf102030",
                dataFormatada,
                null,
                "America/Sao_Paulo",
                codigo,
                true
        );
    }

    private String trim(Object value) {
        return value == null ? null : value.toString().trim();
    }

    /**
     * Busca o linking code do usuário na API externa.
     */
    private String recuperarLinkingCode(String email, String token) {

        LinkingCodeWrapper responseWrapper = webClientlinkClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiProperties.getCodeUrl())
                        .queryParam("email", email)
                        .build())
                .header("Authorization", "Bearer " + token)
                .header("companyId", "750a4df3-28ca-41f0-a02a-ddc502dddefb")
                .retrieve()


                // Tratamento de Exceções
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(body -> {
                                    int status = clientResponse.statusCode().value();
                                    switch (status) {
                                        case 401:
                                            return Mono.error(new AuthenticationException("Token expirado ou ausente: " + body));
                                        case 422:
                                            return Mono.error(new BusinessException("Problema nos dados enviados: " + body));
                                        default:
                                            return Mono.error(new BusinessException("Erro 4xx desconhecido: " + body));
                                    }
                                }))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ServerException("Erro interno no servidor")))


                // Desserializa a resposta
                .bodyToMono(LinkingCodeWrapper.class)
                .block();

        // Valida retorno
        if (responseWrapper == null || responseWrapper.data() == null || responseWrapper.data().isEmpty()) {
            throw new BusinessException("Não foi possível recuperar o código de vinculação para o e-mail: " + email);
        }

        // Lista as respostas retornadas da API
      List<LinkingCodeResponse> codigo = responseWrapper.data().stream().toList();

        // Recria o wrapper (mantendo sua lógica original)
         LinkingCodeWrapper wrapper = new LinkingCodeWrapper(responseWrapper.data());

        // Retorna o Linking Code do primeiro item
        return wrapper.data().get(0).linking_code();
    }
}
