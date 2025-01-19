package com.loja.api_vendas.produto;

import com.loja.api_vendas.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/produto")
@Tag(name = "Produto")
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Operation(summary = "Listar todos os produtos cadastrados", method = "GET")
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar produto por id",method = "GET")
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable @NotNull UUID id){
        return ResponseEntity.ok(service.buscarPorId(id).orElseThrow(()->new NotFoundException("Produto não cadastrado no banco de dados")));
    }

    @Operation(summary = "Buscar produto por descrição", method = "GET")
    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<Produto>> buscarPorDescricao(@PathVariable @NotEmpty String descricao){
        return ResponseEntity.ok(service.buscarPorDescricao(descricao));
    }

    @Operation(summary = "Buscar produto por código de barras")
    @GetMapping("/codigoBarras/{codigoBarras}")
    public ResponseEntity<Produto> buscaPorCodigoBarras(@PathVariable @NotNull Long codigoBarras){
        return ResponseEntity.ok(service.buscarPorCodigoBarras(codigoBarras).orElseThrow(()->new NotFoundException("Produto não cadastrado no banco de dados")));
    }

    @Operation(summary = "Cadastrar novo produto")
    @PostMapping
    public ResponseEntity<Produto> cadastrar(@Valid @RequestBody Produto produto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(produto));
    }

    @Operation(summary = "Atualizar produto")
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable UUID id, @Valid @RequestBody ProdutoDto produto){
        return ResponseEntity.ok(service.atualizar(id,produto));
    }

    @Operation(summary = "Deletar produto", method = "DELETE")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
