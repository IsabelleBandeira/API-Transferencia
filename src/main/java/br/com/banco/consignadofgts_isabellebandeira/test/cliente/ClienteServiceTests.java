package br.com.banco.consignadofgts_isabellebandeira.test.cliente;

import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoAtualizadoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoCadastradoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoDeletadoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoEncontradoException;
import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ClienteRepository;
import br.com.banco.consignadofgts_isabellebandeira.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Testes Unitários
@ExtendWith(MockitoExtension.class)
public class ClienteServiceTests {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private final String TEST_NOME = "Isabelle Bandeira";
    private Cliente cliente;

    @BeforeEach
    void setUp(){
        ContaCorrente conta = new ContaCorrente();
        cliente = Cliente.builder().nome(TEST_NOME).numContacorrente(conta).build();
    }

    //CADASTRO
    @Test
    void testCadastrarClienteComSucesso() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.cadastrarCliente(cliente);

        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testCadastrarClienteComErro() {
        when(clienteRepository.save(cliente)).thenThrow(new ClienteNaoCadastradoException("Erro ao cadastrar cliente"));

        ClienteNaoCadastradoException thrown = assertThrows(
                ClienteNaoCadastradoException.class,
                () -> clienteService.cadastrarCliente(cliente)
        );

        assertEquals("Erro ao cadastrar cliente", thrown.getMessage());
        verify(clienteRepository, times(1)).save(cliente);
    }

    //LISTA
    @Test
    void testListarClientesComSucesso() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> result = clienteService.listarTodosOsClientes();

        assertNotNull(result);
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testListarClienteComErro() {
        when(clienteRepository.findAll()).thenThrow(new ClienteNaoEncontradoException("Não há clientes cadastrados no momento."));

        ClienteNaoEncontradoException thrown = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> clienteService.listarTodosOsClientes()
        );

        assertEquals("Não há clientes cadastrados no momento.", thrown.getMessage());
        verify(clienteRepository, times(1)).findAll();
    }

    //BUSCA POR ID
    @Test
    void testBuscarClientePorIdComSucesso() {
        Long id = cliente.getIdCliente();
        when(clienteRepository.findById(id)).thenReturn(Optional.ofNullable(cliente));

        Cliente result = clienteService.buscarPorId(id);

        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void testBuscarClientePorIdComErro() {
        Long id = cliente.getIdCliente();
        when(clienteRepository.findById(id)).thenThrow(new ClienteNaoEncontradoException("Cliente não encontrado."));

        ClienteNaoEncontradoException thrown = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> clienteService.buscarPorId(id)
        );

        assertEquals("Cliente não encontrado.", thrown.getMessage());
        verify(clienteRepository, times(1)).findById(id);
    }

    //ATUALIZAR
    @Test
    void testAtualizarClienteComSucesso() {
        cliente.setNome("Isabelle Beatriz Dias Bandeira");
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.atualizarCliente(cliente);

        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testAtualizarClienteComErro() {
        cliente.setNome("Isabelle Beatriz Dias Bandeira");
        when(clienteRepository.save(cliente)).thenThrow(new ClienteNaoAtualizadoException("Erro ao atualizar cliente"));

        ClienteNaoAtualizadoException thrown = assertThrows(
                ClienteNaoAtualizadoException.class,
                () -> clienteService.atualizarCliente(cliente)
        );

        assertEquals("Erro ao atualizar cliente", thrown.getMessage());
        verify(clienteRepository, times(1)).save(cliente);
    }


    //DELETAR
    @Test
    void testDeletarClienteComSucesso() {
        Long id = cliente.getIdCliente();
        clienteService.deletarCliente(id);

        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeletarClienteComErro_NotFound() {
        Long id = cliente.getIdCliente();

        doThrow(new EmptyResultDataAccessException(1)).when(clienteRepository).deleteById(id);

        ClienteNaoEncontradoException thrown = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> clienteService.deletarCliente(id)
        );

        assertEquals("Cliente não encontrado.", thrown.getMessage());
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeletarClienteComErro() {
        Long id = cliente.getIdCliente();

        doThrow(new DataIntegrityViolationException("Foreign key constraint")).when(clienteRepository).deleteById(id);

        ClienteNaoDeletadoException thrown = assertThrows(
                ClienteNaoDeletadoException.class,
                () -> clienteService.deletarCliente(id)
        );

        assertEquals("Não foi possível deletar o cliente: Existem dados relacionados ao cliente.", thrown.getMessage());
        verify(clienteRepository, times(1)).deleteById(id);
    }

    //BUSCA POR CONTA CORRENTE
    @Test
    void testBuscarClientePorContaCorrenteComSucesso() {
        ContaCorrente conta = cliente.getNumContacorrente();
        when(clienteRepository.findByNumContacorrente_NumContaCorrente(conta.getNumContaCorrente())).thenReturn(Optional.ofNullable(cliente));

        Cliente result = clienteService.buscarPorContaCorrente(conta.getNumContaCorrente());

        assertNotNull(result);
        assertEquals(cliente, result);
        verify(clienteRepository, times(1)).findByNumContacorrente_NumContaCorrente(conta.getNumContaCorrente());
    }

    @Test
    void testBuscarClientePorContaCorrenteComErro() {
        ContaCorrente conta = cliente.getNumContacorrente();
        when(clienteRepository.findByNumContacorrente_NumContaCorrente(conta.getNumContaCorrente())).thenThrow(new ClienteNaoEncontradoException("Nenhum cliente encontrado com essa conta corrente."));

        ClienteNaoEncontradoException thrown = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> clienteService.buscarPorContaCorrente(conta.getNumContaCorrente())
        );

        assertEquals("Nenhum cliente encontrado com essa conta corrente.", thrown.getMessage());
        verify(clienteRepository, times(1)).findByNumContacorrente_NumContaCorrente(conta.getNumContaCorrente());
    }

}
