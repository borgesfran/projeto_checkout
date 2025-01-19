package com.loja.api_vendas.produto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.utils.Utils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Entity
@Table(name = "produto", uniqueConstraints = {@UniqueConstraint(columnNames = {"codigoBarras"})})
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String descricao;

    @Column(unique = true, nullable = false)
    private Long codigoBarras;

    private BigDecimal valor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime ultimaAtualizacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataCadastro;

    @Transient
    private Integer totalEstoque;

    public Produto(ProdutoEntradaDto dto){
        this.descricao = dto.descricao();
        this.codigoBarras = dto.codigoBarras();
        this.valor = dto.valor();
        this.ultimaAtualizacao = LocalDateTime.now();
        this.dataCadastro = LocalDateTime.now();
        this.totalEstoque = dto.quantidade();
    }

    public Produto toUpperCase(){
        this.descricao = this.descricao.toUpperCase();
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }

    public Produto normalizaDescricao(){
        this.descricao = Utils.removerAcentos(this.descricao);
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }

    public Produto atualizar(ProdutoDto dto){
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }
}
