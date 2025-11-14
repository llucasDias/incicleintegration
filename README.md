# Integração Protheus x Incicle

Projeto em **Spring Boot (Java)** que conecta o ERP **TOTVS Protheus** à plataforma **Incicle**, automatizando o envio de dados de colaboradores, convites e cadastros de usuários.

---

## Funcionalidades

- Autenticação automática via token (Incicle API)
- Busca de colaboradores no banco Protheus (SQL Server)
- Envio de payloads no formato JSON para a API Incicle
- Registro e convite automático de novos usuários
- Estrutura modular com camadas de Repository, Service e Controller
- Front-end simples com **Thymeleaf** para interação manual

---

## Tecnologias

- **Java 21**
- **Spring Boot 3**
- **Spring WebFlux (WebClient)**
- **Spring JDBC**
- **SQL Server**
- **Thymeleaf**
- **Jackson (ObjectMapper)**
- **JUnit 5**
- **Mockito**

---

## Arquitetura do Projeto

```
└── main
├── java
│ └── com.lucas.incicleintegration
│ ├── service
│ │ ├── ImportService.java
│ │ ├── RegisterService.java
│ │ └── TokenService.java
│ ├── exception
│ │ ├── AuthenticationException.java
│ │ ├── BusinessException.java
│ │ ├── ServerException.java
│ │ └── ValidationException.java
│ ├── dto
│ └── repository
│ └── ProtheusRepository.java
└── resources
├── templates
│ └── index.html
└── static
└── css
└── style.css
```

---

## Exceções Personalizadas

O sistema utiliza exceções específicas para tratar erros de forma organizada:

| Exceção | Quando ocorre |
|----------|----------------|
| AuthenticationException | Token inválido ou expirado |
| BusinessException | Erro 422 ou violação de regra de negócio |
| ServerException | Erro 500 da API externa |
| ValidationException | Dados inválidos retornados do Protheus |

---
