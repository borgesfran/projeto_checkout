package com.loja.api_vendas.cliente;

import com.loja.api_vendas.exception.NotFoundException;
import com.loja.api_vendas.exception.ServerException;
import com.loja.api_vendas.exception.SubscribeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private Cliente cliente;

    @BeforeEach
    void init(){
        cliente = new Cliente(
                UUID.randomUUID(),
                "João das Neves",
                "62237636036",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void aoBuscarTodosRetornarLista(){
        when(repository.findAll()).thenReturn(Collections.singletonList(cliente));

        List<Cliente> clientes = service.listarTodos();

        Assertions.assertNotEquals(new ArrayList<>(), clientes);
        Assertions.assertTrue(clientes.contains(cliente));
    }

    @Test
    void aoBuscarPorIdRetornarCliente(){
        when(repository.findById(any())).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = service.buscarPorId(UUID.randomUUID());

        Assertions.assertNotEquals(Optional.empty(), resultado);
        Assertions.assertTrue(resultado.isPresent());
    }

    @Test
    void aoBuscarPorCPFRetornarCliente(){
        when(repository.findByCpf(any())).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = service.buscarPorCPF("62237636036");

        Assertions.assertNotEquals(Optional.empty(), resultado);
        Assertions.assertTrue(resultado.isPresent());
    }

    @Test
    void aoCadastrarRetornarCliente(){
        when(repository.findByCpf(any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(cliente);

        Cliente resultado = service.cadastrar(cliente);

        Assertions.assertEquals(cliente, resultado);
    }

    @Test
    void aoCadastrarRetornarSubscribeException(){
        when(repository.findByCpf(any())).thenReturn(Optional.of(cliente));

        assertThrows(SubscribeException.class,()->service.cadastrar(cliente));
    }

    @Test
    void aoCadastrarRetornarServerException(){
        when(repository.findByCpf(any())).thenReturn(Optional.empty());
        doThrow(ServerException.class).when(repository).save(any());

        assertThrows(ServerException.class,()->service.cadastrar(cliente));
    }

    @Test
    void aoAtualiarRetornarCliente(){
        when(repository.findById(any())).thenReturn(Optional.of(cliente));

        cliente.setNome("João Targaryen");
        when(repository.save(any())).thenReturn( cliente);

        ClienteDto dto = new ClienteDto("João Targaryen");

        Cliente resultado = service.atualizar(cliente.getId(),dto);

        Assertions.assertEquals(cliente, resultado);
    }

    @Test
    void aoAtualiarRetornarNotFoundException(){
        when(repository.findById(any())).thenReturn(Optional.empty());

        ClienteDto dto = new ClienteDto("João Targaryen");

        assertThrows(NotFoundException.class,()->service.atualizar(cliente.getId(),dto));
    }

    @Test
    void aoDeletarRetornarNotFoundException(){
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()->service.deletar(cliente.getId()));
    }


}
