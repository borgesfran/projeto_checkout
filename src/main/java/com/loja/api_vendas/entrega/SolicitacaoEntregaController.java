package com.loja.api_vendas.entrega;

import com.loja.api_vendas.estoque.EstoqueService;
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
@RequestMapping(value = "/entrega")
@Tag(name = "Entrega")
@CrossOrigin("*")
public class SolicitacaoEntregaController {

    @Autowired
    private SolicitacaoEntregaService service;

    @Operation(summary = "Buscar solicitacão de entrega cadastrada para pedido")
    @GetMapping("/pedido/{id_pedido}")
    public ResponseEntity<EntregaSalvaDto> buscarSolicitacaoEntregaDoPedido(@NotNull @PathVariable UUID id_pedido){
        return ResponseEntity.ok(service.buscarPorPedido(id_pedido));
    }

    @Operation(summary = "Buscar solicitacões de entrega cadastrada para transportadora")
    @GetMapping("/transportadora/{id_transportadora}")
    public ResponseEntity<List<EntregaSalvaDto>> buscarSolicitacoesEntregaPorTransportadora(@NotNull @PathVariable UUID id_transportadora){
        return ResponseEntity.ok(service.buscarPorTransportadora(id_transportadora));
    }

    @Operation(summary = "Buscar solicitacão de entrega por id")
    @GetMapping("/{id}")
    public ResponseEntity<EntregaSalvaDto> buscarSolicitacoesEntregaPorId(@NotNull@PathVariable UUID id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar solicitacão de entrega")
    @PostMapping
    public ResponseEntity<EntregaDto> cadastrarSolicitacoesEntrega(@Valid @RequestBody SolicitacaoEntregaDto dto){
        return ResponseEntity.ok(service.cadastrar(dto));
    }

    @Operation(summary = "Alterar solicitacão de entrega")
    @PutMapping("/alterar/{id}")
    public ResponseEntity<EntregaDto> alterarSolicitacoesEntrega(@NotNull @PathVariable UUID id, @Valid @RequestBody SolicitacaoEntregaDto dto){
        return ResponseEntity.ok(service.alterar(id,dto));
    }

    @Operation(summary = "Cancelar solicitacão de entrega")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarSolicitacoesEntrega(@NotNull @PathVariable UUID id){
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }


}
