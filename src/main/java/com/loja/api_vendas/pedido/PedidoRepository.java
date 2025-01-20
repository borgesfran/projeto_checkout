package com.loja.api_vendas.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    @Query("select p from Pedido p where p.cliente.id =:id_cliente and p.faturado =:is_faturado")
    List<Pedido> findByCliente(UUID id_cliente, boolean is_faturado);

    @Query("select p from Pedido p where p.cliente.id =:id_cliente")
    List<Pedido> findByCliente(UUID idCliente);
}
