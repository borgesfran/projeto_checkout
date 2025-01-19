package com.loja.api_vendas.produto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProdutoEntradaDto(
        @NotBlank(message = "A descrição do produto deve ser informada")
        String descricao,
        Long codigoBarras,
        BigDecimal valor,
        Integer quantidade
) {
}
