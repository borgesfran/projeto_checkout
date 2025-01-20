package com.loja.api_vendas.pagamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    @Query("select p from Pagamento p where p.pedido.id in :list")
    List<Pagamento> findByPedidos(List<UUID> list);

    @Query("select p from Pagamento p where p.pedido.id =:id_pedido")
    Optional<Pagamento> findByPedido(UUID id_pedido);
}
