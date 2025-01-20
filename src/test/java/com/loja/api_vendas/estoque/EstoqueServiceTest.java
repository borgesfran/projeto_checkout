package com.loja.api_vendas.estoque;

import com.loja.api_vendas.produto.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EstoqueServiceTest {

    @Mock
    private EstoqueRepository repository;

    @InjectMocks
    private EstoqueService service;

    private Estoque estoque;

    private Produto produto;

    @BeforeEach
    void init(){
        produto = new Produto(
                UUID.randomUUID(),
                "ARROZ TIO JORGE 1KG",
                15978L,
                new BigDecimal("5.75"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                10
        );

        estoque = new Estoque(
                UUID.randomUUID(),
                produto,
                10,
                TipoMovimentacao.ENTRADA,
                LocalDateTime.now()
        );
    }

    @Test
    void aoRegistrarMovimentacaoRetornarEstoque(){
        when(repository.save(any())).thenReturn(estoque);

        Estoque resultado = service.registrarMovimentacao(produto,TipoMovimentacao.ENTRADA,10);

        Assertions.assertEquals(estoque,resultado);
    }

    @Test
    void aoBuscarTotalEstoquePorProdutoRetornarQuantidade(){
        when(repository.findByProduto(any())).thenReturn(Collections.singletonList(estoque));

        Integer resultado = service.totalEstoquePorProduto(produto.getId());

        Assertions.assertEquals(estoque.getQuantidade(),resultado);
    }

}
