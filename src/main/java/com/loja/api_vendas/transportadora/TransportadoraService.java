package com.loja.api_vendas.transportadora;

import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TransportadoraService {

    @Autowired
    private TransportadoraRepository repository;

    public List<Transportadora> listarTodas(){
        return repository.findAll();
    }

    public Optional<Transportadora> buscarPorId(UUID id){
        return repository.findById(id);
    }

    public List<Transportadora> buscarPorNome(String nome){
        return repository.findByNomeLike("%" + Utils.removerAcentos(nome.toUpperCase())+"%");
    }

    private Transportadora salvar(Transportadora transportadora){
        try {
            return repository.save(transportadora.normalizaNome().toUpperCase());
        }catch (Exception e){
            log.error("Erro ao processar dados de produto: {}",e.getMessage());
            throw new ServerException("Erro ao processar dados de produto. Tente novamente");
        }
    }

    public Transportadora cadastrar(Transportadora transportadora){
        return this.salvar(transportadora);
    }

    public Transportadora atualizar(UUID id, Transportadora transportadora){
        if(repository.findById(id).isEmpty())
            throw new NotFoundException("Transportadora com o id "+ id + " nâo encontrado no banco de dados");

        transportadora.setId(id);
        return this.salvar(transportadora);
    }

    public void deletar(UUID id){
        var transportadora = repository.findById(id);

        if(transportadora.isEmpty())
            throw new NotFoundException("Transportadora com o id "+ id + " nâo encontrado no banco de dados");

        repository.delete(transportadora.get());
    }
}
