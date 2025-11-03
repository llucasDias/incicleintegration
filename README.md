# 🔗 Integração Protheus x Incicle

Projeto em **Spring Boot (Java)** que conecta o ERP **TOTVS Protheus** à plataforma **Incicle** para automatizar o envio de dados de colaboradores, convites e cadastros de usuários.

## 🚀 Funcionalidades

- Autenticação automática via token (Incicle API)
- Busca de colaboradores no banco Protheus (SQL Server)
- Envio de payloads no formato JSON para a API Incicle
- Registro e convite automático de novos usuários
- Logging completo das requisições (Spring + SLF4J)
- Estrutura modular com camadas de Repository, Service e Controller

## 🧩 Tecnologias

- **Java 21**
- **Spring Boot 3**
- **Spring WebFlux (WebClient)**
- **Spring JDBC**
- **Docker + Docker Compose**
- **SQL Server**
- **Lombok**
- **Jackson (ObjectMapper)**
- **JUnit 5**
