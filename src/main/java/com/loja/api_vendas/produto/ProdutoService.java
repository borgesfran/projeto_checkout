package com.loja.api_vendas.produto;

import com.loja.api_vendas.estoque.EstoqueService;
import com.loja.api_vendas.estoque.TipoMovimentacao;
import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.exception.SubscribeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private EstoqueService estoqueService;

    public List<Produto> listarTodos(){
        List<Produto> produtos = repository.findAll();

        for (Produto produto : produtos) {
            produto.setTotalEstoque(estoqueService.totalEstoquePorProduto(produto.getId()));
        }

        return produtos;
    }

    public Optional<Produto> buscarPorId(UUID id){
        var produto = repository.findById(id);

        produto.ifPresent(prd->prd.setTotalEstoque(estoqueService.totalEstoquePorProduto(prd.getId())));

        return produto;
    }

    public List<Produto> buscarPorDescricao(String descricao){
        List<Produto> produtos = repository.findByDescricaoLike("%"+ descricao + "%");

        for (Produto produto : produtos) {
            produto.setTotalEstoque(estoqueService.totalEstoquePorProduto(produto.getId()));
        }

        return produtos;
    }

    public Optional<Produto> buscarPorCodigoBarras(Long codigo){
        var produto = repository.findByCodigoBarras(codigo);

        produto.ifPresent(prd->prd.setTotalEstoque(estoqueService.totalEstoquePorProduto(prd.getId())));

        return produto;
    }

    private Produto salvar(Produto produto){
        try {
            return repository.save(produto.normalizaDescricao().toUpperCase());
        }catch (Exception e){
            log.error("Erro ao processar dados de produto: {}",e.getMessage());
            throw new ServerException("Erro ao processar dados de produto. Tente novamente");
        }
    }

    @Transactional(rollbackFor = {ServerException.class})
    public Produto cadastrar(ProdutoEntradaDto dto){
        if(repository.findByCodigoBarras(dto.codigoBarras()).isPresent()){
            throw new SubscribeException("Produto já cadastrado na base de dados");
        }

        var produto = this.salvar(new Produto(dto));
        if(!Optional.ofNullable(estoqueService.registrarMovimentacao(produto, TipoMovimentacao.ENTRADA, dto.quantidade())).isPresent()){
            throw new ServerException("Erro ao registrar entrada de produto");
        }

        return produto;
    }

    public Produto atualizar(UUID id, ProdutoDto dto){
        var produto = repository.findById(id);

        produto.ifPresentOrElse(prd-> prd.atualizar(dto),
                ()->{throw new NotFoundException("Produto com o id "+ id + " nâo encontrado no banco de dados");});

        return this.salvar(produto.get());
    }

    public Produto registrarNovaEntradaProduto(UUID id, Integer quantidade){
        var produto = repository.findById(id);

        produto.ifPresentOrElse(prd-> {
                    estoqueService.registrarMovimentacao(prd, TipoMovimentacao.ENTRADA, quantidade);
                },
                ()->{throw new NotFoundException("Produto com o id "+ id + " nâo encontrado no banco de dados");});

        return this.buscarPorId(id).get();
    }

    public void deletar(UUID id){
       //todo validar depois se nâo tem compra para esse produto
        var produto = repository.findById(id);

        produto.ifPresentOrElse(prd->{
            if(estoqueService.totalEstoquePorProduto(prd.getId()) > 0)
                throw new SubscribeException("Produto não pode ser deletado pois há estoque registrado para o mesmo");
            repository.delete(prd);
        }, ()->{throw new NotFoundException("Produto com o id "+ id + " nâo encontrado no banco de dados");});
    }

}
