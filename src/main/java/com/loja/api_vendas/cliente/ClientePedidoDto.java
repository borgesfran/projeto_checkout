package com.loja.api_vendas.cliente;
import java.util.UUID;

public record ClientePedidoDto(
        UUID id,
        String nome,
        String cpf
) {
    public ClientePedidoDto(Cliente cliente){
        this(cliente.getId(),cliente.getNome(),cliente.getCpf());
    }
}
