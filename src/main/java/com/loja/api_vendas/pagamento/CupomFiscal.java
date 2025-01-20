package com.loja.api_vendas.pagamento;

import com.loja.api_vendas.entrega.EntregaSalvaDto;
import com.loja.api_vendas.pedido.ItemPedidoSalvoDto;

import java.math.BigDecimal;
import java.util.List;

public record CupomFiscal(
        String nomeEmpresa,
        String cnpj,
        String nomeCliente,
        String cpf,
        List<ItemPedidoSalvoDto> itensPedido,
        EntregaSalvaDto entrega,
        BigDecimal valorPedido,
        BigDecimal valorEntrega,
        BigDecimal valorTotal,
        TipoPagamento tipoPagamento
) {
}
