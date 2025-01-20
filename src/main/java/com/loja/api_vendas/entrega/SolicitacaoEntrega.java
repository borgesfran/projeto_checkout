package com.loja.api_vendas.entrega;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.pedido.Pedido;
import com.loja.api_vendas.transportadora.Transportadora;
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
@Table(name = "entrega")
public class SolicitacaoEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Pedido pedido;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_transportadora", nullable = false)
    private Transportadora transportadora;

    private String endereco;

    private BigDecimal valorEntrega;

    SolicitacaoEntrega(Pedido pedido, Transportadora transportadora, Endereco endereco){
        this.pedido = pedido;
        this.transportadora = transportadora;
        this.endereco = endereco.toString();
        this.valorEntrega = transportadora.getValorFrete();
    }
}
