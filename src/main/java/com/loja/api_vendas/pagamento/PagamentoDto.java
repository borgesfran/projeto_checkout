package com.loja.api_vendas.pagamento;

import java.math.BigDecimal;
import java.util.UUID;

public record PagamentoDto(
        UUID id_pedido,
        UUID id_entrega,
        BigDecimal valorTotal
) {

    public PagamentoDto(Pagamento pagamento){
        this(pagamento.getPedido().getId(), pagamento.getSolicitacaoEntrega().getId(),pagamento.getValorTotal());
    }
}
