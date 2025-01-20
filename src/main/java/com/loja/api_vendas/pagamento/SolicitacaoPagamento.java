package com.loja.api_vendas.pagamento;

import java.util.UUID;

public record SolicitacaoPagamento(
        UUID id_pedido,
        TipoPagamento tipoPagamento
) {
}
