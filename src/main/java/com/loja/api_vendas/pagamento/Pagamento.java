package com.loja.api_vendas.pagamento;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.entrega.SolicitacaoEntrega;
import com.loja.api_vendas.pedido.Pedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Entity
@Table(name = "pagamento")
public class Pagamento implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @JoinColumn(name = "id_entrega", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private SolicitacaoEntrega solicitacaoEntrega;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    Pagamento(Pedido pedido, SolicitacaoEntrega entrega, TipoPagamento tipoPagamento){
        this.pedido = pedido;
        this.solicitacaoEntrega = entrega;
        this.valorTotal = BigDecimal.ZERO;
        this.valorTotal.add(pedido.getValorTotal()).add(entrega.getValorEntrega());
        this.tipoPagamento = tipoPagamento;
    }
}
