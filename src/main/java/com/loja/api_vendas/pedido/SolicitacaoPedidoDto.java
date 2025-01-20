package com.loja.api_vendas.pedido;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SolicitacaoPedidoDto(
        @NotNull(message = "Id do cliente deve ser informado")
        UUID id_cliente,
        @NotEmpty(message = "Os itens do pedido devem ser informados")
        List<ItemPedidoDto> itens
) {
}
