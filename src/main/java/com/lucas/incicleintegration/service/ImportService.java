package com.lucas.incicleintegration.service;



import com.lucas.incicleintegration.config.ApiProperties;
import com.lucas.incicleintegration.dto.invite.InviteRequest;
import com.lucas.incicleintegration.dto.invite.InviteRequestWrapper;
import com.lucas.incicleintegration.dto.invite.InviteResponseWrapper;
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

    public ImportService(
            ProtheusRepository protheusRepository,
            @Qualifier("inviteClient") WebClient inviteClient,
            ApiProperties apiProperties,
            TokenService tokenService) {
        this.protheusRepository = protheusRepository;
        this.inviteClient = inviteClient;
        this.apiProperties = apiProperties;
        this.tokenService = tokenService;
    }

    /**
     * Metodo principal que importa colaboradores do Protheus e envia convite via API externa.
     * @param matricula código do colaborador para busca no Protheus
     */
    public void importarColaboradores(String matricula) {

        // Buscar colaboradores no Protheus pelo RA (matricula)
        List<Map<String, Object>> resultado = protheusRepository.buscarColaborador(matricula);

        // Excessão customizada
        if (resultado.isEmpty()) {
            throw new ValidationException("Nenhum colaborador encontrado");
        }

        // Obter token de autenticação via TokenService
        String token = tokenService.getToken();


        // Mapear os dados do Protheus para DTO de envio
        List<InviteRequest> listaInvite = resultado.stream()
                .map(this::mapearColaborador)
                .toList();

        InviteRequestWrapper wrapper = new InviteRequestWrapper(listaInvite);

        //Chamada ao endpoint de envio de convites
        InviteResponseWrapper responseWrapper = inviteClient.post()
                .uri(apiProperties.getInviteUrl())
                .header("Authorization", "Bearer " + token)
                .header("companyId", "750a4df3-28ca-41f0-a02a-ddc502dddefb")
                .bodyValue(wrapper)
                .retrieve()


                // --- TRATAMENTO DE EXCEÇÕES
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
                .bodyToMono(InviteResponseWrapper.class)
                .block();
    }

    /**
     * Mapeia os dados do Protheus para o DTO InviteRequest
     **/
    private InviteRequest mapearColaborador(Map<String, Object> c) {
        Integer salario = c.get("RA_SALARIO") != null
                ? ((Number) c.get("RA_SALARIO")).intValue()
                : 0;

        String sexo = validarSexo((String) c.get("RA_SEXO"));
        String dataFormatada = formatarData((String) c.get("RA_ADMISSA"));


        return new InviteRequest(
                trim(c.get("RA_EMAIL")),
                trim(c.get("RA_NOME")),
                null,
                null,
                null,
                null,
                null,
                salario,
                "FULL_TIME",
                null,
                null,
                null,
                null,
                null,
                sexo
        );
    }

    /**
     * Tratamento para retirar espaços em brancos vindos do Protheus.
     */

    private String trim(Object value) {
        return value == null ? null : value.toString().trim();
    }
}



