package com.loja.api_vendas.transportadora;

import com.loja.api_vendas.exception.NotFoundException;
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
@RequestMapping(value = "/transportadora")
@Tag(name = "Transportadora")
@CrossOrigin("*")
public class TransportadoraController {

    @Autowired
    private TransportadoraService service;

    @Operation(summary = "Buscar transportadoras disponíveis para entregas")
    @GetMapping()
    public ResponseEntity<List<Transportadora>> buscarTransportadoras(){
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar transportadora por id")
    @GetMapping("/{id}")
    public ResponseEntity<Transportadora> buscarTransportadoraPorId(@NotNull @PathVariable UUID id){
        return ResponseEntity.ok(service.buscarPorId(id).orElseThrow(()->new NotFoundException("Transportadora não está cadastrada no banco de dados")));
    }

    @Operation(summary = "Buscar transportadora por nome")
    @GetMapping("/{nome}")
    public ResponseEntity<List<Transportadora>> buscarTransportadoraPorNome(@NotNull @PathVariable String nome){
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @Operation(summary = "Cadastrar transportadora")
    @PostMapping
    public ResponseEntity<Transportadora> cadastrar(@Valid @RequestBody Transportadora transportadora){
        return ResponseEntity.ok(service.cadastrar(transportadora));
    }

    @Operation(summary = "Atualizar transportadora")
    @PutMapping("/{id}")
    public ResponseEntity<Transportadora> atualizar(@NotNull @PathVariable UUID id, @Valid @RequestBody Transportadora transportadora){
        return ResponseEntity.ok(service.atualizar(id,transportadora));
    }

    @Operation(summary = "Deletar transportadora")
    @DeleteMapping("/{id}")
    public ResponseEntity<Transportadora> deletar(@NotNull @PathVariable UUID id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
