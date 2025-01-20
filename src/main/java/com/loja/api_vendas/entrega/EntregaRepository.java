package com.loja.api_vendas.entrega;

import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntregaRepository extends JpaRepository<SolicitacaoEntrega, UUID> {

    @Query("select e from SolicitacaoEntrega e where e.transportadora.id =:id_transportadora and e.cancelada =:is_cancelada")
    List<SolicitacaoEntrega> findByTransportadora(UUID id_transportadora, boolean is_cancelada);

    @Query("select e from SolicitacaoEntrega e where e.pedido.id =:id_pedido and e.cancelada =:is_cancelada")
    Optional<SolicitacaoEntrega> findByPedido(UUID id_pedido, boolean is_cancelada);
}
