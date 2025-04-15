package br.com.banco.consignadofgts_isabellebandeira.test.conta;

import br.com.banco.consignadofgts_isabellebandeira.exception.conta.ContaNaoAtualizadaException;
import br.com.banco.consignadofgts_isabellebandeira.exception.conta.ContaNaoEncontradaException;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ContaCorrenteServiceTests {
    
    @Mock
    private ContaCorrenteRepository contaRepository;
    
    @InjectMocks
    private ContaCorrenteService contaService;
    private ContaCorrente conta;

    @BeforeEach
    public void setUp(){
        conta = ContaCorrente.builder().numContaCorrente(1L).saldo(100.0).build();
    }
    
    //LISTA
    @Test
    void testListarContaCorrentesComSucesso() {
        when(contaRepository.findAll()).thenReturn(List.of(conta));

        List<ContaCorrente> result = contaService.listarTodasAsContas();

        assertNotNull(result);
        verify(contaRepository, times(1)).findAll();
    }

    @Test
    void testListarContaCorrenteComErro() {
        when(contaRepository.findAll()).thenThrow(new ContaNaoEncontradaException("Não há contas cadastradas no momento."));

        ContaNaoEncontradaException thrown = assertThrows(
                ContaNaoEncontradaException.class,
                () -> contaService.listarTodasAsContas()
        );

        assertEquals("Não há contas cadastradas no momento.", thrown.getMessage());
        verify(contaRepository, times(1)).findAll();
    }

    //BUSCA POR ID
    @Test
    void testBuscarContaPorIdComSucesso() {
        Long id = conta.getNumContaCorrente();
        when(contaRepository.findById(id)).thenReturn(Optional.ofNullable(conta));

        ContaCorrente result = contaService.buscarPorId(id);

        assertNotNull(result);
        assertEquals(conta, result);
        verify(contaRepository, times(1)).findById(id);
    }

    @Test
    void testBuscarContaPorIdComErro() {
        Long id = conta.getNumContaCorrente();
        when(contaRepository.findById(id)).thenThrow(new ContaNaoEncontradaException("Conta não encontrada."));

        ContaNaoEncontradaException thrown = assertThrows(
                ContaNaoEncontradaException.class,
                () -> contaService.buscarPorId(id)
        );

        assertEquals("Conta não encontrada.", thrown.getMessage());
        verify(contaRepository, times(1)).findById(id);
    }

    //CADASTRO
    @Test
    void testCadastrarContaComSucesso() {
        when(contaRepository.save(any(ContaCorrente.class))).thenReturn(conta);

        ContaCorrente result = contaService.cadastrarContaCorrente();

        assertNotNull(result);
        assertEquals(conta, result);
        verify(contaRepository, times(1)).save(any(ContaCorrente.class));
    }

    //DEPOSITAR
    @Test
    void testDepositarSaldoContaCorrenteComSucesso() {
        Double valor = 100.0;
        when(contaRepository.save(conta)).thenReturn(conta);

        ContaCorrente result = contaService.depositarSaldoContaCorrente(valor, conta);

        assertNotNull(result);
        assertEquals(conta, result);
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    void testDepositarSaldoContaCorrenteComErro() {
        Double valor = 100.0;
        when(contaRepository.save(conta)).thenThrow(new ContaNaoAtualizadaException("Erro ao depositar saldo na conta."));

        ContaNaoAtualizadaException thrown = assertThrows(
                ContaNaoAtualizadaException.class,
                () -> contaService.depositarSaldoContaCorrente(valor, conta)
        );

        assertEquals("Erro ao depositar saldo na conta.", thrown.getMessage());
        verify(contaRepository, times(1)).save(conta);
    }


}
