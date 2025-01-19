package com.loja.api_vendas.produto;

import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.exception.SubscribeException;
import com.loja.api_vendas.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> listarTodos(){
        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(UUID id){
        return repository.findById(id);
    }

    public List<Produto> buscarPorDescricao(String descricao){
        return repository.findByDescricaoLike("%"+ descricao + "%");
    }

    public Optional<Produto> buscarPorCodigoBarras(Long codigo){
        return repository.findByCodigoBarras(codigo);
    }

    private Produto salvar(Produto produto){
        try {
            produto.setDataCadastro(LocalDateTime.now());
            return repository.save(produto.normalizaDescricao().toUpperCase());
        }catch (Exception e){
            log.error("Erro ao processar dados de produto: {}",e.getMessage());
            throw new ServerException("Erro ao processar dados de produto. Tente novamente");
        }
    }

    public Produto cadastrar(Produto produto){
        if(repository.findByCodigoBarras(produto.getCodigoBarras()).isPresent()){
            throw new SubscribeException("Produto já cadastrado na base de dados");
        }

        return this.salvar(produto);
    }

    public Produto atualizar(UUID id, ProdutoDto dto){
        var produto = repository.findById(id);

        produto.ifPresentOrElse(prd-> prd.atualizar(dto),
                ()->{throw new NotFoundException("Produto com o id "+ id + " nâo encontrado no banco de dados");});

        return this.salvar(produto.get());
    }

    public void deletar(UUID id){
       //todo validar depois se nâo tem compra ou estoque para esse produto
        var produto = repository.findById(id);

        produto.ifPresentOrElse(prd->{
            repository.delete(prd);
        }, ()->{throw new NotFoundException("Produto com o id "+ id + " nâo encontrado no banco de dados");});
    }


}
