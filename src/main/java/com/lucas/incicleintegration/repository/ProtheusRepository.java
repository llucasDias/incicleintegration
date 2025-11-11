package com.lucas.incicleintegration.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class ProtheusRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProtheusRepository (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> buscarColaborador(String matricula) {
        String sql = """
                	SELECT
                   	RA_EMAIL,
                   	RA_NOME,
                   	RA_ADMISSA,
                   	RA_SALARIO,
                    RA_NUMCP,
                   	RA_CIC,
                   	RA_RG,
                   	RA_ORGEMRG,
                   	RA_PIS,
                   	RA_SEXO,
                   	RA_NASC,
                   	RA_NUMCELU
                   	FROM SRA010
                    WHERE RA_MAT = ?
             """;


        return jdbcTemplate.queryForList(sql, matricula);
    }
}