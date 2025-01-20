package com.loja.api_vendas.pedido;

import com.loja.api_vendas.cliente.ClienteService;
import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.produto.Produto;
import com.loja.api_vendas.produto.ProdutoPedidoDto;
import com.loja.api_vendas.produto.ProdutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    public Optional<Pedido> buscarPorId(UUID id){
        Optional<Pedido> pedido = repository.findById(id);

        if(pedido.isPresent()){
            List<ItemPedido> itens = itemPedidoService.listarItensDePedido(id);
            pedido.get().setItensPedido(itens);
        }

        return pedido;
    }

    public List<Pedido> buscarPedidosNaoFaturadosPorCliente(UUID id_cliente){
        List<Pedido> pedidos = repository.findByCliente(id_cliente,false);

        pedidos.forEach(pedido -> {
            List<ItemPedido> itens = itemPedidoService.listarItensDePedido(pedido.getId());
            pedido.setItensPedido(itens);
        });

        return pedidos;
    }

    public List<Pedido> buscarPedidosFaturadosPorCliente(UUID id_cliente){
        List<Pedido> pedidos = repository.findByCliente(id_cliente,true);

        pedidos.forEach(pedido -> {
            List<ItemPedido> itens = itemPedidoService.listarItensDePedido(pedido.getId());
            pedido.setItensPedido(itens);
        });

        return pedidos;
    }

    public List<Pedido> buscarPedidosPorCliente(UUID id_cliente){
        List<Pedido> pedidos = repository.findByCliente(id_cliente);

        pedidos.forEach(pedido -> {
            List<ItemPedido> itens = itemPedidoService.listarItensDePedido(pedido.getId());
            pedido.setItensPedido(itens);
        });

        return pedidos;
    }

    @Transactional(rollbackFor = {ServerException.class})
    private Pedido salvarPedido(Pedido pedido){
        try {
            var pedidoSalvo = repository.save(pedido);

            pedidoSalvo.getItensPedido().forEach(item-> {
                item.setPedido(pedidoSalvo);
                itemPedidoService.salvar(item);
            });

            pedido.getItensPedido().forEach(item->{
                produtoService.registrarSaidaProduto(item.getProduto().getId(),item.getQuantidade());
            });

            return pedidoSalvo;
        }catch (Exception e){
            log.error("Erro ao processar dados de pedido: {}",e.getMessage());
            throw new ServerException("Erro ao processar dados de pedido. Tente novamente");
        }
    }

    public PedidoDto cadastrar(SolicitacaoPedidoDto dto) {
        var cliente = clienteService.buscarPorId(dto.id_cliente());

        if(cliente.isEmpty())
            throw new NotFoundException("Cliente com o id " + dto.id_cliente() + " nâo encontrado no banco de dados");

        if(!cliente.get().isAtivo())
            throw new ServerException("Impossível continuar com o pedido, cliente está inativo");

        List<UUID> idsProdutosPedido = dto.itens().stream().map(ItemPedidoDto::id_produto).toList();
        List<Produto> produtos = produtoService.buscarProdutos(idsProdutosPedido);

        List<UUID> idsNaoCadastrados = idsProdutosPedido.stream().filter(id -> !produtos.stream().map(Produto::getId).toList().contains(id)).toList();

        if (!idsNaoCadastrados.isEmpty())
            throw new ServerException("Erro ao cadastrar pedido: não há registro para os produtos " + idsNaoCadastrados);

        List<UUID> produtosSemEstoque = new ArrayList<>();

        List<ItemPedido> itens = new ArrayList<>();
        dto.itens().forEach(item -> produtos.stream()
                        .filter(prd -> {
                            if(prd.getTotalEstoque() == 0 || prd.getTotalEstoque() < item.quantidade()){
                                produtosSemEstoque.add(prd.getId());
                            }

                            return prd.getId().equals(item.id_produto()) && prd.getTotalEstoque() >= item.quantidade();
                        })
                        .findFirst()
                        .ifPresent(produto -> itens.add(new ItemPedido(produto, item.quantidade())))
                );

        if (!produtosSemEstoque.isEmpty())
            throw new ServerException("Erro ao cadastrar pedido: não há estoque para os produtos " + produtosSemEstoque);

        Pedido pedido = this.salvarPedido(new Pedido(cliente.get(), itens));

        List<ItemPedidoSalvoDto> listaItens = pedido.getItensPedido().stream()
                .map(item->new ItemPedidoSalvoDto(item))
                .toList();

        return new PedidoDto(pedido,listaItens);
    }

    public Pedido adicionarItem(UUID id_pedido, List<ItemPedidoDto> itens){
        var pedido = repository.findById(id_pedido);

        if(pedido.isEmpty())
            throw new NotFoundException("Pedido com o id " + id_pedido + " nâo encontrado no banco de dados");

        if(pedido.get().isFaturado())
            throw new ServerException("Pedido não pode ser alterado pois já foi faturado");

        List<UUID> idsProdutosPedido = itens.stream().map(ItemPedidoDto::id_produto).toList();
        List<Produto> produtos = produtoService.buscarProdutos(idsProdutosPedido);
        List<ItemPedido> itensPedido = new ArrayList<>();

        itens.forEach(item -> {
            produtos.stream()
                    .filter(prd -> prd.getId().equals(item.id_produto()))
                    .findFirst()
                    .ifPresent(produto -> itensPedido.add(new ItemPedido(produto, item.quantidade())));
        });

        pedido.get().getItensPedido().addAll(itensPedido);

        return this.salvarPedido(pedido.get());
    }

    public Pedido removerItem(UUID id_pedido, UUID id_item){
        var pedido = repository.findById(id_pedido);

        if(pedido.isEmpty())
            throw new NotFoundException("Pedido com o id " + id_pedido + " nâo encontrado no banco de dados");

        if(pedido.get().isFaturado())
            throw new ServerException("Pedido não pode ser alterado pois já foi faturado");

        var itemPedido = pedido.get().getItensPedido().stream()
                .filter(item->item.getId().equals(id_item)).findFirst();

        itemPedido.ifPresentOrElse(item->{
            itemPedidoService.deletarItemPedido(item);
            pedido.get().getItensPedido().remove(item);
        },()->{
            throw new ServerException("Erro ao remover item de pedido. Tente novamente." );
        });

        return pedido.get();
    }

    public void cancelarPedido(UUID id_pedido){
        var pedido = repository.findById(id_pedido);

        if(pedido.isEmpty())
            throw new NotFoundException("Pedido com o id " + id_pedido + " nâo encontrado no banco de dados");

        if(pedido.get().isFaturado())
            throw new ServerException("Pedido não pode ser cancelado pois já foi faturado");

        List<ItemPedido> itens = itemPedidoService.listarItensDePedido(id_pedido);

        itens.forEach(itemPedido -> itemPedidoService.deletarItemPedido(itemPedido));

        repository.delete(pedido.get());
    }

    public void faturarPedido(UUID id_pedido){
        var pedido = repository.findById(id_pedido);

        if(pedido.isEmpty())
            throw new NotFoundException("Pedido com o id " + id_pedido + " nâo encontrado no banco de dados");

        pedido.get().setFaturado(true);
        this.salvarPedido(pedido.get());
    }

}
