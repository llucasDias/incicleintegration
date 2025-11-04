package com.lucas.incicleintegration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.protheus")
    public DataSource protheusDataSource() {
        return DataSourceBuilder.create().build();

    }

}
