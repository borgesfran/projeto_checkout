package com.loja.api_vendas.estoque;

import com.loja.api_vendas.produto.Produto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository repository;

    public List<Estoque> registroPorProduto(UUID id_produto){
        return repository.findByProduto(id_produto);
    }

    public Estoque registrarMovimentacao(Produto produto, TipoMovimentacao tipoMovimentacao, Integer quantidade){
        return repository.save(new Estoque(produto,tipoMovimentacao,quantidade));
    }

    public Integer totalEntradasProduto(List<Estoque> estoque ){
        return estoque.stream().filter(e->e.getTipoMovimentacao().equals(TipoMovimentacao.ENTRADA)).mapToInt(e-> e.getQuantidade()).sum();
    }

    public Integer totalSaidasProduto(List<Estoque> estoque ){
        return estoque.stream().filter(e->e.getTipoMovimentacao().equals(TipoMovimentacao.SAIDA)).mapToInt(e-> e.getQuantidade()).sum();
    }

    public Integer totalEstoquePorProduto(UUID id_produto){
        List<Estoque> estoque = repository.findByProduto(id_produto);
        Integer totalEntradas = this.totalEntradasProduto(estoque);
        Integer totalSaidas = this.totalSaidasProduto(estoque);
        return totalEntradas - totalSaidas;
    }

    public Integer totalEstoquePorProduto(List<Estoque> estoque){
        Integer totalEntradas = this.totalEntradasProduto(estoque);
        Integer totalSaidas = this.totalSaidasProduto(estoque);
        return totalEntradas - totalSaidas;
    }
}
