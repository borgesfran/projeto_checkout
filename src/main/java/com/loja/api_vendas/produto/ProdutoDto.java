package com.loja.api_vendas.produto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProdutoDto(
        @NotBlank(message = "A descrição do produto deve ser informada")
        String descricao,
        BigDecimal valor
) {
}
