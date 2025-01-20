package com.loja.api_vendas.entrega;

import com.loja.api_vendas.transportadora.Transportadora;

import java.math.BigDecimal;
import java.util.UUID;

public record EntregaSalvaDto(
        UUID id,
        UUID id_pedido,
        Transportadora transportadora,
        String endereco,
        BigDecimal valorTotal
) {
    public EntregaSalvaDto(SolicitacaoEntrega entrega){
        this(entrega.getId(),entrega.getPedido().getId(),entrega.getTransportadora(),entrega.getEndereco(),entrega.getValorEntrega());
    }
}
