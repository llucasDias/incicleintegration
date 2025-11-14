package com.lucas.incicleintegration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /**
     * Cria e configura o DataSource do Protheus com base nas
     * propriedades externas do ambiente de configuração.
     *
     * @return um DataSource configurado para o Protheus
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "app.datasource.protheus")
    public DataSource protheusDataSource() {

        return DataSourceBuilder.create().build();
    }
}