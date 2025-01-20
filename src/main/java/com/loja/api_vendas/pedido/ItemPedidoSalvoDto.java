package com.loja.api_vendas.pedido;

import com.loja.api_vendas.produto.ProdutoPedidoDto;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoSalvoDto(
        UUID id,
        ProdutoPedidoDto produto,
        BigDecimal valorUnitario,
        Integer quantidade,
        BigDecimal valorTotal
) {
    public ItemPedidoSalvoDto(ItemPedido item){
        this(item.getId(),new ProdutoPedidoDto(item.getProduto()),item.getValorUnitario(),item.getQuantidade(),item.getValorTotal());
    }
}
