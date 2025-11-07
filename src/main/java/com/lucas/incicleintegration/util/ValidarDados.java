package com.lucas.incicleintegration.util;

/** Classe para validação do Salario conforme API do Incicle e Formatar a Data **/

public class ValidarDados {

    public static long validarSalario(Object valor) {
        if (valor == null) return 0L;

        double salario = ((Number) valor).doubleValue();

        if (salario < 0) salario = 0;

        return Math.round(salario*100);
           }


    public static String validarSexo(Object sexo) {
      if (sexo == "M") return "MALE_CIS";
      if (sexo == "F") return  "FEMALE_CIS";
      return "NOT_DECLARED";
    }

    public static String formatarData(Object data) {
        if (data == null) return null;


        if (data instanceof java.util.Date date) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }


        if (data instanceof String str) {
            str = str.trim();
            if (str.isEmpty()) return null;
            if (str.length() == 8) {
                String ano = str.substring(0, 4);
                String mes = str.substring(4, 6);
                String dia = str.substring(6, 8);
                return ano + "-" + mes + "-" + dia;
            } else {
                return str;
            }
        }


        if (data instanceof Number num) {
            String str = String.valueOf(num.longValue());
            if (str.length() == 8) {
                String ano = str.substring(0, 4);
                String mes = str.substring(4, 6);
                String dia = str.substring(6, 8);
                return ano + "-" + mes + "-" + dia;
            }
            return str;
        }

        // Caso não seja nenhum tipo reconhecido
        return data.toString();
    }
}