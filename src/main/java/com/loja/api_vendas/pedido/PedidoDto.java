package com.loja.api_vendas.pedido;

import com.loja.api_vendas.cliente.ClientePedidoDto;
import com.loja.api_vendas.produto.ProdutoPedidoDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PedidoDto(
        UUID id,
        ClientePedidoDto cliente,
        List<ItemPedidoSalvoDto> itens,
        BigDecimal valorTotal
) {
    public PedidoDto(Pedido pedido, List<ItemPedidoSalvoDto> listaItens){
        this(pedido.getId(),new ClientePedidoDto(pedido.getCliente()),listaItens,pedido.getValorTotal());
    }
}
