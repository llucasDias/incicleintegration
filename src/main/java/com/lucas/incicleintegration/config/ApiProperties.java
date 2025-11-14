package com.lucas.incicleintegration.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * Classe responsável por mapear as propriedades de configuração da API do Incicle.
 * <p>
 * Essas propriedades são carregadas a partir do arquivo {@code application.yml} ou {@code application.properties},
 * utilizando o prefixo <b>api</b>.
 * </p>
 *
 * Exemplo de configuração:
 * <pre>
 * api:
 *   base-url: https://core.backend.inciclebeta.com/api/v1
 *   auth-url: https://schedule.backend.inciclebeta.com/api/auth/authenticate
 *   invite-url: /engineering/collaborators/invites/
 *   code-url: /engineering/collaborators/linking-code/
 *   register-url: /engineering/collaborators/register/
 *   username: seu_usuario
 *   password: sua_senha
 *   company-id: 123456
 *   content-type: application/json
 *   accept: application/json
 * </pre>
 *
 * <p>Essas propriedades são utilizadas em conjunto com o {@link org.springframework.web.reactive.function.client.WebClient}
 * para realizar chamadas à API do Incicle.</p>
 *
 * @author Lucas
 * @since 1.0
 */


@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    /** URL base da API do Incicle */
    private String baseUrl;

    /** Endpoint de autenticação (geração de token) */
    private String authUrl;

    /** Endpoint para envio de convites de colaboradores */
    private String inviteUrl;

    /** Endpoint para obtenção do código de vinculação (linking code) */
    private String codeUrl;

    /** Endpoint de registro de colaboradores */
    private String registerUrl;

    /** Usuário utilizado para autenticação */
    private String username;

    /** Senha utilizada para autenticação */
    private String password;

    /** Identificador único da empresa dentro da plataforma Incicle */
    private String companyId;

    /** Cabeçalho padrão utilizado nas requisições (opcional) */
    private String defaultHeader;

    /** Valor padrão do cabeçalho customizado (opcional) */
    private String defaultHeaderValue;

    /** Tipo de conteúdo aceito nas requisições */
    private String contentType;

    /** Tipo de conteúdo aceito nas respostas */
    private String accept;


    private String jobUrl;

    private String sectorUrl;

    private String levelUrl;


    // ======================
    // Getters e Setters
    // ======================

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getAuthUrl() { return authUrl; }
    public void setAuthUrl(String authUrl) { this.authUrl = authUrl; }

    public String getInviteUrl() { return inviteUrl; }
    public void setInviteUrl(String inviteUrl) { this.inviteUrl = inviteUrl; }

    public String getCodeUrl() { return codeUrl; }
    public void setCodeUrl(String codeUrl) { this.codeUrl = codeUrl; }

    public String getRegisterUrl() { return registerUrl; }
    public void setRegisterUrl(String registerUrl) { this.registerUrl = registerUrl; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getDefaultHeader() { return defaultHeader; }
    public void setDefaultHeader(String defaultHeader) { this.defaultHeader = defaultHeader; }

    public String getDefaultHeaderValue() { return defaultHeaderValue; }
    public void setDefaultHeaderValue(String defaultHeaderValue) { this.defaultHeaderValue = defaultHeaderValue; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getAccept() { return accept; }
    public void setAccept(String accept) { this.accept = accept; }

    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) { this.jobUrl = jobUrl; }

    public String getLevelUrl() { return levelUrl; }
    public void setLevelUrl(String levelUrl) { this.levelUrl = levelUrl; }

    public String getSectorUrl() { return sectorUrl; }
    public void setSectorUrl(String sectorUrl) { this.sectorUrl = sectorUrl; }
}
