package com.loja.api_vendas.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.produto.Produto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Entity
@Table(name = "item_pedido")
public class ItemPedido {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Pedido pedido;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    private BigDecimal valorUnitario;

    private Integer quantidade;

    private BigDecimal valorTotal;

    public ItemPedido(Produto produto, Integer quantidade){
        this.produto = produto;
        this.valorUnitario = produto.getValor();
        this.quantidade = quantidade;
        this.valorTotal = produto.getValor().multiply(BigDecimal.valueOf(quantidade));
    }
}
