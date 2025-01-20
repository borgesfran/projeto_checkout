package com.loja.api_vendas.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemPedidoDto(
        @NotNull(message = "O id do produto deve ser informado")
        UUID id_produto,
        @NotNull(message = "A quantidade do produto deve ser informada")
        Integer quantidade
) {
}
