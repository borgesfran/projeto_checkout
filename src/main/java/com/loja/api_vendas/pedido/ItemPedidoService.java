package com.loja.api_vendas.pedido;

import com.loja.api_vendas.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ItemPedidoService {
    @Autowired ItemPedidoRepository repository;

    public ItemPedido salvar(ItemPedido item){
        return repository.save(item);
    }

    public List<ItemPedido> listarItensDePedido(UUID id_pedido){
        return repository.findByPedido(id_pedido);
    }

    public BigDecimal valorTotalItensDePedido(UUID id_pedido){
        List<ItemPedido> itens = repository.findByPedido(id_pedido);
        return itens.stream().map(item->item.getValorTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deletarItemPedido(ItemPedido item){
        var itemPedido = repository.findById(item.getId());

        if(itemPedido.isEmpty())
            throw new NotFoundException("Item com o id "+ item.getId() + " nâo encontrado no banco de dados");

        repository.delete(item);
    }

}
