package com.loja.api_vendas.transportadora;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.utils.Utils;
import jakarta.persistence.*;
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
@Table(name = "transportadora")
public class Transportadora {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String nome;

    private BigDecimal valorFrete;

    public Transportadora toUpperCase(){
        this.nome = this.nome.toUpperCase();
        return this;
    }

    public Transportadora normalizaNome(){
        this.nome = Utils.removerAcentos(this.nome);
        return this;
    }
}
