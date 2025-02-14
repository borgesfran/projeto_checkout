package com.loja.api_vendas.entrega;

import com.loja.api_vendas.cliente.ClienteService;
import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.pedido.PedidoService;
import com.loja.api_vendas.transportadora.TransportadoraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SolicitacaoEntregaService {

    @Autowired
    private EntregaRepository repository;

    @Autowired
    private TransportadoraService transportadoraService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    public List<EntregaSalvaDto> buscarPorTransportadora(UUID id_transportadora){
        return repository.findByTransportadora(id_transportadora,false).stream().map(EntregaSalvaDto::new).toList();
    }

    public List<EntregaSalvaDto> buscarCanceladasPorTransportadora(UUID id_transportadora){
        return repository.findByTransportadora(id_transportadora, true).stream().map(EntregaSalvaDto::new).toList();
    }

    public EntregaSalvaDto buscarPorPedido(UUID id_pedido){
        return new EntregaSalvaDto(repository.findByPedido(id_pedido,false).get());
    }

    public EntregaSalvaDto buscarCanceladasPorPedido(UUID id_pedido){
        return new EntregaSalvaDto(repository.findByPedido(id_pedido,true).get());
    }

    public Optional<SolicitacaoEntrega> buscarSolicitacaoEntregaPorPedido(UUID id_pedido){
        return repository.findByPedido(id_pedido,false);
    }

    public Optional<SolicitacaoEntrega> buscarSolicitacaoEntregaCanceladasPorPedido(UUID id_pedido){
        return repository.findByPedido(id_pedido,true);
    }

    public EntregaSalvaDto buscarPorId(UUID id){
        var entrega = repository.findById(id);

        if(entrega.isEmpty())
            throw new NotFoundException("Esta entrega não está cadastrada no banco de dados");

        return new EntregaSalvaDto(entrega.get());
    }

    private SolicitacaoEntrega salvar(SolicitacaoEntrega solicitacaoEntrega){
        try {
            var entrega = repository.findByPedido(solicitacaoEntrega.getPedido().getId(),true);

            if(entrega.isPresent()){
                entrega.get().setCancelada(false);
                solicitacaoEntrega = entrega.get();
            }

            return repository.saveAndFlush(solicitacaoEntrega);
        }catch (Exception e){
            log.error("Erro ao processar dados de produto: {}",e.getMessage());
            throw new ServerException("Erro ao processar dados de produto. Tente novamente");
        }
    }

    private SolicitacaoEntrega validarSolicitacaoEntrega(SolicitacaoEntregaDto dto){
        var transportadora = transportadoraService.buscarPorId(dto.id_transportadora());

        if(!transportadora.isPresent())
            throw new ServerException("Transportadora não cadastrada no banco de dados");

        var pedido = pedidoService.buscarPorId(dto.id_pedido());

        if(!pedido.isPresent())
            throw new ServerException("Pedido não cadastrado no banco de dados");

        return new SolicitacaoEntrega(pedido.get(),transportadora.get(),dto.endereco());
    }

    public EntregaDto cadastrar (SolicitacaoEntregaDto dto){

        if(!repository.findByPedido(dto.id_pedido(),false).isEmpty())
            throw new ServerException("Entrega já cadastrada para esse pedido");

        var entrega = this.salvar(this.validarSolicitacaoEntrega(dto));

        return new EntregaDto(entrega,dto.endereco());
    }

    public EntregaDto alterar(UUID id, SolicitacaoEntregaDto dto){
        var entrega = repository.findById(id);
        if(entrega.isEmpty())
            throw new NotFoundException("Entrega não está cadastrada na base de dados");

        SolicitacaoEntrega solicitacaoEntrega = this.validarSolicitacaoEntrega(dto);
        solicitacaoEntrega.setId(id);

        this.salvar(solicitacaoEntrega);

        return new EntregaDto(solicitacaoEntrega,dto.endereco());
    }

    public void cancelar(UUID id){
        var entrega = repository.findById(id);

        if(entrega.isEmpty())
            throw new NotFoundException("Entrega não está cadastrada na base de dados");

        if(entrega.get().getPedido().isFaturado())
            throw new ServerException("Impossível prosseguir, entrega já faturada");

        entrega.get().setCancelada(true);
        this.salvar(entrega.get());
    }
}

