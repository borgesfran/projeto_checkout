package com.loja.api_vendas.pedido;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/pedido")
@Tag(name = "Pedido")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @Operation(summary = "Listar todos os pedidos ainda não pagos para um cliente", method = "GET")
    @GetMapping("/cliente/{id_cliente}")
    public ResponseEntity<List<Pedido>> listarPedidosNaoFaturadosDoCliente(@PathVariable @NotNull UUID id_cliente){
        return ResponseEntity.ok(service.buscarPedidosNaoFaturadosPorCliente(id_cliente));
    }

    @Operation(summary = "Listar todos os pedidos já pagos para um cliente", method = "GET")
    @GetMapping("/faturado/cliente/{id_cliente}")
    public ResponseEntity<List<Pedido>> listarPedidosFaturadosDoCliente(@PathVariable @NotNull UUID id_cliente){
        return ResponseEntity.ok(service.buscarPedidosFaturadosPorCliente(id_cliente));
    }

    @Operation(summary = "Cadastrar novo pedido")
    @PostMapping
    public ResponseEntity<PedidoDto> cadastrar(@Valid @RequestBody SolicitacaoPedidoDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @Operation(summary = "Adicionar novos itens em pedido não faturado")
    @PutMapping("/adicionar/item/{id_pedido}")
    public ResponseEntity<Pedido> adicionarItensEmPedido(@PathVariable @NotNull UUID id_pedido, @Valid @RequestBody List<ItemPedidoDto> itens){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarItem(id_pedido,itens));
    }

    @Operation(summary = "Remover item em pedido não faturado")
    @PutMapping("/remover/item/{id_pedido}/{id_item}")
    public ResponseEntity<Pedido> removerItemDePedido(@PathVariable @NotNull UUID id_pedido, @PathVariable @NotNull UUID id_item){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.removerItem(id_pedido,id_item));
    }

    @Operation(summary = "Cancelar pedido", method = "DELETE")
    @DeleteMapping("/{id_pedido}")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id_pedido){
        service.cancelarPedido(id_pedido);
        return ResponseEntity.noContent().build();
    }

}
