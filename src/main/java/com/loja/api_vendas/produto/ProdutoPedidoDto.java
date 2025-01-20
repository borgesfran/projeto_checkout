package com.loja.api_vendas.produto;

import java.util.UUID;

public record ProdutoPedidoDto(
        UUID id,
        String descricao,
        Long codigoBarras
) {
    public ProdutoPedidoDto(Produto produto){
        this(produto.getId(),produto.getDescricao(),produto.getCodigoBarras());
    }
}
