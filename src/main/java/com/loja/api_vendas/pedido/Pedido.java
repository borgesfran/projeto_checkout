package com.loja.api_vendas.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.cliente.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Entity
@Table(name = "pedido")
public class Pedido {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    private BigDecimal valorTotal;

    private boolean faturado;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataCriacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataFaturamento;

    @Transient
    private List<ItemPedido> itensPedido;

    Pedido(Cliente cliente, List<ItemPedido> itensPedido){
        this.cliente = cliente;
        this.itensPedido = itensPedido;
        this.valorTotal = itensPedido.stream().map(ItemPedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.faturado = false;
        this.dataCriacao = LocalDateTime.now();
    }
}
