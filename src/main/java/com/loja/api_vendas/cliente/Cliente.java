package com.loja.api_vendas.cliente;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.loja.api_vendas.utils.Utils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Entity
@Table(name = "cliente", uniqueConstraints = {@UniqueConstraint(columnNames = {"codigoBarras"})})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotBlank(message = "O nome do cliente não pode ser vazio")
    private String nome;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "O CPF do cliente não pode ser vazio")
    private String cpf;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean ativo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataCadastro;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime ultimaAtualizacao;

    public Cliente habilitar(){
        this.ativo = true;
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }

    public Cliente desabilitar(){
        this.ativo = false;
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }

    public Cliente toUpperCase(){
        this.nome = this.nome.toUpperCase();
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }

    public Cliente removeMascaraCpf(){
        this.cpf = Utils.removeMascaraCpf(this.cpf);
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }

    public Cliente preencherDataCadastro(){
        this.setDataCadastro(LocalDateTime.now());
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }

    public Cliente atualizar(ClienteDto dto){
        this.nome = dto.nome();
        this.ultimaAtualizacao = LocalDateTime.now();
        return this;
    }
}
