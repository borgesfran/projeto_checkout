package com.loja.api_vendas.cliente;

import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/cliente")
@Tag(name = "Cliente")
@CrossOrigin("*")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @Operation(summary = "Listar todos os clientes cadastrados", method = "GET")
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }


    @Operation(summary = "Buscar cliente por id", method = "GET")
    @GetMapping("/id/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable @NotNull UUID id){
        return ResponseEntity.ok(service.buscarPorId(id).orElseThrow(()->new NotFoundException("Cliente não cadastrado no banco de dados")));
    }

    @Operation(summary = "Buscar cliente por cpf", method = "GET")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> buscarPorCpf(@PathVariable @NotBlank String cpf){
        return ResponseEntity.ok(service.buscarPorCPF(Utils.removeMascaraCpf(cpf)).orElseThrow(()->new NotFoundException("Cliente não cadastrado no banco de dados")));
    }

    @Operation(summary = "Cadastrar novo cliente")
    @PostMapping
    public ResponseEntity<Cliente> salvar(@Valid @RequestBody Cliente cliente){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(cliente));
    }

    @Operation(summary = "Atualizar cliente")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable UUID id, @Valid @RequestBody ClienteDto cliente){
        return ResponseEntity.ok(service.atualizar(id,cliente));
    }

    @Operation(summary = "Desativar cliente", method = "DELETE")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
