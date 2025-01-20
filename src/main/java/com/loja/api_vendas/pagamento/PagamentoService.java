package com.loja.api_vendas.pagamento;

import com.loja.api_vendas.cliente.ClienteService;
import com.loja.api_vendas.entrega.SolicitacaoEntregaService;
import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.pedido.ItemPedidoSalvoDto;
import com.loja.api_vendas.pedido.Pedido;
import com.loja.api_vendas.pedido.PedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private SolicitacaoEntregaService solicitacaoEntregaService;

    @Autowired
    private ClienteService clienteService;

    private final String EMPRESA = "GRUPO MATEUS";
    private final String CNPJ = "24.990.777/0001-09";


    public List<PagamentoDto> buscarPagamentosPorCliente(UUID id_cliente){
        List<Pedido> pedidos = pedidoService.buscarPedidosPorCliente(id_cliente);

        if(pedidos.isEmpty())
            throw new NotFoundException("Não há registros de pagamentos realizados por esse cliente");

        List<Pagamento> pagamentos = repository.findByPedidos(pedidos.stream().map(Pedido::getId).toList());

        if(pagamentos.isEmpty())
            throw new NotFoundException("Não há registros de pagamentos realizados por esse cliente");


        return pagamentos.stream().map(PagamentoDto::new).toList();
    }

    public PagamentoDto buscarPagamentoPorPedido(UUID id_pedido){
        var pagamento = repository.findByPedido(id_pedido);

        if(pagamento.isEmpty())
            throw new NotFoundException("Não há registro de pagamento para esse pedido");

        return new PagamentoDto(pagamento.get());
    }

    private Pagamento capturaInformacoesDoPedido(SolicitacaoPagamento solicitacaoPagamento, Pedido pedido){
        if(pedido.isFaturado())
            throw new ServerException("Impossível prosseguir, pagamento já efetuado");

        var entrega = solicitacaoEntregaService.buscarSolicitacaoEntregaPorPedido(solicitacaoPagamento.id_pedido());

        if(entrega.isEmpty())
            throw new NotFoundException("Impossível prosseguir, não há registros de solicitação de entrega para o pedido " + solicitacaoPagamento.id_pedido());

        return new Pagamento(pedido,entrega.get(),solicitacaoPagamento.tipoPagamento());

    }

    @Transactional(rollbackFor = {ServerException.class})
    private PagamentoDto salvarPagamento(Pagamento pagamento){
        try {
            pagamento = repository.save(pagamento);
            pedidoService.faturarPedido(pagamento.getPedido().getId());
            return new PagamentoDto(pagamento);
        }catch (Exception e){
            log.error("Erro ao processar dados de produto: {}",e.getMessage());
            throw new ServerException("Erro ao processar dados de produto. Tente novamente");
        }
    }

    public PagamentoDto realizarPagamento(SolicitacaoPagamento solicitacaoPagamento){
        var pedido = pedidoService.buscarPorId(solicitacaoPagamento.id_pedido());

        if(pedido.isEmpty())
            throw new NotFoundException("Pedido com o id " + solicitacaoPagamento.id_pedido() + " nâo encontrado no banco de dados");

        Pagamento pagamento = this.capturaInformacoesDoPedido(solicitacaoPagamento,pedido.get());

        return this.salvarPagamento(pagamento);
    }

    public CupomFiscal gerarCumpomFiscalPedido(UUID id_pedido){
        var pedido = pedidoService.buscarPorId(id_pedido);

        if(pedido.isEmpty())
            throw new NotFoundException("Pedido com o id " + id_pedido+ " nâo encontrado no banco de dados");

        var pagamento = repository.findByPedido(id_pedido);

        if(pagamento.isEmpty())
            throw new NotFoundException("Não há registro desse pagamento no banco de dados");

        var entrega = solicitacaoEntregaService.buscarPorPedido(id_pedido);

        if(entrega == null)
            throw new NotFoundException("Impossível prosseguir, não há registros de solicitação de entrega para o pedido " + id_pedido);

        var cliente = clienteService.buscarPorId(pedido.get().getCliente().getId());

        List<ItemPedidoSalvoDto> listaItens = pedido.get().getItensPedido().stream()
                .map(ItemPedidoSalvoDto::new).toList();

        return new CupomFiscal(
                EMPRESA,
                CNPJ,
                cliente.get().getNome(),
                cliente.get().getCpf(),
                listaItens,
                entrega,
                pedido.get().getValorTotal(),
                entrega.valorTotal(),
                pagamento.get().getValorTotal(),
                pagamento.get().getTipoPagamento()
        );
    }
}
