package com.loja.api_vendas.produto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoDto(
        @NotBlank(message = "A descrição do produto deve ser informada")
        String descricao,
        BigDecimal valor
) {
}
