package com.loja.api_vendas.cliente;

import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.exception.SubscribeException;
import com.loja.api_vendas.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> listarTodos(){
        return repository.findAll();
    }

    public Optional<Cliente> buscarPorId(UUID id){
        return repository.findById(id);
    }

    public Optional<Cliente> buscarPorCPF(String cpf){
        return repository.findByCpf(cpf);
    }

    private Cliente salvar(Cliente cliente){
        try{
            return repository.save(cliente.toUpperCase().removeMascaraCpf());
        }catch (Exception e){
            log.error("Erro ao processar dados de cliente:{}", e.getMessage());
            throw new ServerException("Erro ao processar dados de cliente. Tente novamente.");
        }
    }

    public Cliente cadastrar(Cliente cliente){
        if(repository.findByCpf(Utils.removeMascaraCpf(cliente.getCpf())).isPresent()){
            throw new SubscribeException("CPF já cadastrado na base de dados");
        }

        return this.salvar(cliente.habilitar().preencherDataCadastro());
    }

    public Cliente atualizar(UUID id, ClienteDto dto){
        var cliente = repository.findById(id);

        cliente.ifPresentOrElse(clnt->clnt.atualizar(dto),
                ()->{throw new NotFoundException("Cliente com o id " + id + " não encontrado");});
        return this.salvar(cliente.get());
    }

    public void deletar(UUID id){
        var cliente = repository.findById(id);

        cliente.ifPresentOrElse(usr -> this.salvar(usr.desabilitar()),
                ()-> {throw new NotFoundException("Cliente com o id " + id + " não encontrado");} );
    }

}
