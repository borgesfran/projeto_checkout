package com.loja.api_vendas.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {

    @Query("select item from ItemPedido item where item.pedido.id =:id_pedido")
    List<ItemPedido> findByPedido(UUID id_pedido);
}
