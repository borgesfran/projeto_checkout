package com.loja.api_vendas.pagamento;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/pagamento")
@Tag(name = "Pagamento")
@CrossOrigin("*")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @Operation(summary = "Listar todos os pagamentos de um cliente", method = "GET")
    @GetMapping("/cliente/{id_cliente}")
    public ResponseEntity<List<PagamentoDto>> listarPagamentosDeCliente(@PathVariable @NotNull UUID id_cliente){
        return ResponseEntity.ok(service.buscarPagamentosPorCliente(id_cliente));
    }

    @Operation(summary = "Buscar pagamento de pedido", method = "GET")
    @GetMapping("/pedido/{id_pedido}")
    public ResponseEntity<PagamentoDto> buscarPagamentoPorPedido(@PathVariable @NotNull UUID id_pedido){
        return ResponseEntity.ok(service.buscarPagamentoPorPedido(id_pedido));
    }

    @Operation(summary = "Realizar pagamento de pedido", method = "GET")
    @PostMapping("/pedido/{id_pedido}")
    public ResponseEntity<PagamentoDto> realizarPagamento(@Valid @RequestBody SolicitacaoPagamento solicitacaoPagamento){
        return ResponseEntity.ok(service.realizarPagamento(solicitacaoPagamento));
    }

    @Operation(summary = "Gerar cupom fiscal para pedido", method = "GET")
    @GetMapping("/cupom/{id_pedido}")
    public ResponseEntity<CupomFiscal> gerarCumpomFiscalPedido(@PathVariable @NotNull UUID id_pedido){
        return ResponseEntity.ok(service.gerarCumpomFiscalPedido(id_pedido));
    }

}
