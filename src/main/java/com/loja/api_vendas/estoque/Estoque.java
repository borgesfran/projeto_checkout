package com.loja.api_vendas.estoque;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.produto.Produto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Entity
@Table(name = "estoque")
public class Estoque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @NotNull
    @Min(value = 0)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoMovimentacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataMovimentacao;

    public Estoque(Produto produto, TipoMovimentacao tipoMovimentacao, Integer quantidade){
        this.produto = produto;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.dataMovimentacao = LocalDateTime.now();
    }

}
