package com.loja.api_vendas.estoque;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, UUID> {

    @Query("select etq from Estoque etq where etq.produto.id =:id_produto")
    List<Estoque> findByProduto(UUID id_produto);
}
