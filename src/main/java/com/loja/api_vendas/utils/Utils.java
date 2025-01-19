package com.loja.api_vendas.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public interface Utils {

    static String removeMascaraCpf(String cpf){
        return cpf.replaceAll("[^\\d]", "");
    }

    static String removerAcentos(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    static String removerCaracteresEspeciais(String str) {
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
}
