package com.loja.api_vendas.cliente;

import jakarta.validation.constraints.NotEmpty;

public record ClienteDto(
        @NotEmpty(message = "O nome do cliente deve ser informado")
        String nome
) {
}
