package com.lucas.incicleintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;



/**
 * Classe responsável por configurar os {@link WebClient}s utilizados
 * nas integrações com a API do Incicle.

 * Cada bean é configurado com base em uma função específica da API,
 * utilizando URLs e cabeçalhos definidos em {@link ApiProperties}.


 * Responsabilidades desta classe:

 *   Definir e injetar URLs base e cabeçalhos comuns
 *   Adicionar filtros de log e tratamento genérico de erros

 *  Erros específicos (ex: token inválido, campos ausentes, etc.)
 * devem ser tratados nas classes de serviço correspondentes.

 * @author Lucas
 * @since 1.0
 */

@Configuration
public class WebClientConfig {

    private final ApiProperties apiProperties;

    public WebClientConfig(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    /** Config de Invite e Registro **/


    @Bean(name = "authClient")
    public WebClient authWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getAuthUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();

    }

    @Bean(name = "inviteClient")
    public WebClient inviteWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();
    }

    @Bean(name = "linkClient")
    public WebClient linkWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();
    }

    @Bean(name="registerClient")
    public WebClient registerWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();
    }






    /** Config de Pós-Registro: Dados Pessoais e Cadastros **/

    @Bean(name = "collaboratorsClient")
    public WebClient collaboratorsClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();
    }


    @Bean(name = "jobClient")
    public WebClient jobClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();
    }

    @Bean(name = "levelClient")
    public WebClient levelClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();
    }

    @Bean(name = "sectorClient")
    public WebClient sectorClient(WebClient.Builder builder) {
        return builder
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Content-Type", apiProperties.getContentType())
                .defaultHeader("Accept", apiProperties.getAccept())
                .build();
    }

}
