package com.lucas.incicleintegration.util;

/** Classe para validação do Salario conforme API do Incicle e Formatar a Data **/

public class ValidarDados {

    public static String validarSexo(Object sexo) {
        if (sexo == null) return "NOT_REVEALED";

        String valor = sexo.toString().trim().toUpperCase();

        if (valor.equals("M")) return "MALE_CIS";
        if (valor.equals("F")) return "FEMALE_CIS";

        return "NOT_REVEALED";
    }

    public static String formatarData(String dataProtheus) {
        if (dataProtheus == null || dataProtheus.trim().isEmpty())
            return null;

        dataProtheus = dataProtheus.trim();

        if (dataProtheus.length() == 8) {
            String ano = dataProtheus.substring(0, 4);
            String mes = dataProtheus.substring(4, 6);
            String dia = dataProtheus.substring(6, 8);
            return ano + "-" + mes + "-" + dia;
        }


        return dataProtheus;
    }


    public static String formataCpf(String cpf) {
        if (cpf == null) return null;

        // Remove tudo que não é número
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido: deve ter 11 dígitos");
        }

        // Formata para xxx.xxx.xxx-xx
        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);
    }
}