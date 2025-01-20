package com.loja.api_vendas.entrega;

import com.loja.api_vendas.transportadora.Transportadora;

import java.math.BigDecimal;
import java.util.UUID;

public record EntregaDto(
        UUID id,
        UUID id_pedido,
        Transportadora transportadora,
        Endereco endereco,
        BigDecimal valorTotal
) {
    public EntregaDto(SolicitacaoEntrega solicitacaoEntrega, Endereco endereco){
        this(
                solicitacaoEntrega.getId(),
                solicitacaoEntrega.getPedido().getId(),
                solicitacaoEntrega.getTransportadora(),
                endereco,
                solicitacaoEntrega.getTransportadora().getValorFrete()
        );
    }
}
