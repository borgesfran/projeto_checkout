package com.loja.api_vendas.entrega;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SolicitacaoEntregaDto(
        @NotNull(message = "Id do cliente deve ser informado")
        UUID id_cliente,
        @NotNull(message = "Id do pedido deve ser informado")
        UUID id_pedido,
        @NotNull(message = "Id da transportadora deve ser informado")
        UUID id_transportadora,
        @NotNull(message = "Endereco deve ser informado")
        Endereco endereco
) {
}
