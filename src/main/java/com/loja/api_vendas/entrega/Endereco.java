package com.loja.api_vendas.entrega;

public record Endereco(
        String cep,
        String logradouro,
        String cidade,
        String estado
) {
}
