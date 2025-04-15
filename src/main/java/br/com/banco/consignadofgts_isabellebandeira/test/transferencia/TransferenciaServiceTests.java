package br.com.banco.consignadofgts_isabellebandeira.test.transferencia;

import br.com.banco.consignadofgts_isabellebandeira.enums.StatusTransferencia;
import br.com.banco.consignadofgts_isabellebandeira.exception.transferencia.TransferenciaInvalidaException;
import br.com.banco.consignadofgts_isabellebandeira.exception.transferencia.TransferenciaNaoEncontradaException;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import br.com.banco.consignadofgts_isabellebandeira.repository.TransferenciaRepository;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import br.com.banco.consignadofgts_isabellebandeira.service.TransferenciaService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceTests {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Mock
    private ContaCorrenteRepository contaRepository;

    @Mock
    private ContaCorrenteService contaService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    private final Long TEST_ID_CONTA_CORRENTE = 1L;
    private final Double TEST_VALOR = 50.0;
    private Transferencia transferencia;
    private ContaCorrente contaCorrente;

    @BeforeEach
    void setUp(){
        transferencia = Transferencia.builder().valorTransferencia(TEST_VALOR)
                .contaCorrenteOrigem(contaCorrente).contaCorrenteDestino(contaCorrente).build();
        contaCorrente = ContaCorrente.builder().numContaCorrente(TEST_ID_CONTA_CORRENTE).build();
    }

    //LISTA TODAS
    @Test
    void testListarTransferenciaComSucesso() {
        when(transferenciaRepository.findAll()).thenReturn(List.of(transferencia));

        List<Transferencia> result = transferenciaService.listarTodasAsTransferencias();

        assertNotNull(result);
        verify(transferenciaRepository, times(1)).findAll();
    }

    @Test
    void testListarTransferenciaComErro() {
        when(transferenciaRepository.findAll()).thenThrow(new TransferenciaNaoEncontradaException("Não há transferências cadastradas no momento."));

        TransferenciaNaoEncontradaException thrown = assertThrows(
                TransferenciaNaoEncontradaException.class,
                () -> transferenciaService.listarTodasAsTransferencias()
        );

        assertEquals("Não há transferências cadastradas no momento.", thrown.getMessage());
        verify(transferenciaRepository, times(1)).findAll();
    }

    //BUSCA POR ID
    @Test
    void testBuscarTransferenciaPorIdComSucesso() {
        Long id = transferencia.getIdTransferencia();
        when(transferenciaRepository.findByIdTransferencia(id)).thenReturn(Optional.ofNullable(transferencia));

        Transferencia result = transferenciaService.buscarPorId(id);

        assertNotNull(result);
        assertEquals(transferencia, result);
        verify(transferenciaRepository, times(1)).findByIdTransferencia(id);
    }

    @Test
    void testBuscaTransferenciaPorIdComErro() {
        Long id = transferencia.getIdTransferencia();
        when(transferenciaRepository.findByIdTransferencia(id))
                .thenThrow(new TransferenciaNaoEncontradaException("Não foi encontrada nenhuma transferência com esse id."));

        TransferenciaNaoEncontradaException thrown = assertThrows(
                TransferenciaNaoEncontradaException.class,
                () -> transferenciaService.buscarPorId(id)
        );

        assertEquals("Não foi encontrada nenhuma transferência com esse id.", thrown.getMessage());
        verify(transferenciaRepository, times(1)).findByIdTransferencia(id);
    }

    //BUSCA POR CONTA CORRENTE
    @Test
    void testBuscarTransferenciaPorContaCorrenteComSucesso() {
        ContaCorrente conta = transferencia.getContaCorrenteOrigem();
        when(transferenciaRepository
                .findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(conta, conta))
                .thenReturn(Optional.of(List.of(transferencia)));

        List<Transferencia> result = transferenciaService.buscarPorContaCorrente(conta);

        assertNotNull(result);
        assertEquals(List.of(transferencia), result);
        verify(transferenciaRepository, times(1))
                .findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(conta, conta);
    }

    @Test
    void testBuscarTransferenciaPorContaCorrenteComErro() {
        ContaCorrente conta = transferencia.getContaCorrenteOrigem();
        when(transferenciaRepository
                .findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(conta, conta))
                .thenThrow(new TransferenciaNaoEncontradaException("Transferência não encontrada"));

        TransferenciaNaoEncontradaException thrown = assertThrows(
                TransferenciaNaoEncontradaException.class,
                () -> transferenciaService.buscarPorContaCorrente(conta)
        );

        assertEquals("Transferência não encontrada", thrown.getMessage());
        verify(transferenciaRepository, times(1))
                .findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(conta, conta);
    }

    //CADASTRO
    @Test
    void testCadastrarTransferenciaComSucesso() {
        when(transferenciaRepository.save(transferencia)).thenReturn(transferencia);

        Transferencia result = transferenciaService.cadastrarTransferencia(transferencia);

        assertNotNull(result);
        assertEquals(transferencia, result);
        verify(transferenciaRepository, times(1)).save(transferencia);
    }

    //FINALIZAR
    @Test
    void testFinalizarTransferenciaComSucesso() {
        when(transferenciaRepository.save(transferencia)).thenReturn(transferencia);

        Transferencia result = transferenciaService.finalizarTransferencia(transferencia);

        assertNotNull(result);
        assertEquals(transferencia, result);
        verify(transferenciaRepository, times(1)).save(transferencia);
    }

    //VALIDA SALDO
    @Test
    void testValidarSaldoContaOrigemComSucesso() {
        ContaCorrente conta = new ContaCorrente();
        conta.setNumContaCorrente(1L);

        transferencia.setContaCorrenteOrigem(conta);
        transferencia.setValorTransferencia(50.0);

        when(contaRepository.findSaldoById(1L)).thenReturn(100.0);

        boolean result = transferenciaService.validarSaldoContaOrigem(transferencia);

        assertTrue(result);
        verify(contaRepository, times(1)).findSaldoById(1L);
    }

    @Test
    void testValidarSaldoContaOrigemComErro() {
        ContaCorrente contaMock = new ContaCorrente();
        contaMock.setNumContaCorrente(1L);

        transferencia.setContaCorrenteOrigem(contaMock);
        transferencia.setValorTransferencia(100.0);

        when(contaRepository.findSaldoById(1L)).thenReturn(50.0);

        boolean result = transferenciaService.validarSaldoContaOrigem(transferencia);

        assertFalse(result);
        verify(contaRepository, times(1)).findSaldoById(1L);
    }

    //VALIDA LIMITE
    @Test
    void testValidarLimiteTransferenciaComSucesso() {
        Boolean result = transferenciaService.validarLimite(transferencia);

        assertTrue(result);
    }

    @Test
    void testValidarLimiteTransferenciaComErro() {
        transferencia.setValorTransferencia(1000.0);
        Boolean result = transferenciaService.validarLimite(transferencia);

        assertFalse(result);
    }

    //VALIDA TRANSFERENCIA
    @Test
    void testValidaTransferenciaComSucesso() {
        ContaCorrente contaOrigem = new ContaCorrente();
        contaOrigem.setNumContaCorrente(1L);
        contaOrigem.setSaldo(100.0);
        transferencia.setContaCorrenteOrigem(contaOrigem);

        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setNumContaCorrente(2L);
        contaDestino.setSaldo(100.0);
        transferencia.setContaCorrenteDestino(contaDestino);

        when(contaRepository.findSaldoById(anyLong())).thenReturn(100.0);

        Transferencia result = transferenciaService.validaTransferencia(transferencia);

        assertNotNull(result);
        assertEquals(transferencia, result);
        verify(contaRepository, times(1)).findSaldoById(anyLong());
    }

    @Test
    void testValidaTransferenciaComErro_MesmaConta() {
        ContaCorrente conta = new ContaCorrente();
        conta.setNumContaCorrente(1L);
        conta.setSaldo(100.0);
        transferencia.setContaCorrenteOrigem(conta);
        transferencia.setContaCorrenteDestino(conta);

        when(contaRepository.findSaldoById(anyLong()))
                .thenThrow(new TransferenciaInvalidaException("Não é possível fazer transferências entre a mesma conta."));

        TransferenciaInvalidaException thrown = assertThrows(
                TransferenciaInvalidaException.class,
                () -> transferenciaService.validaTransferencia(transferencia)
        );

        assertEquals("Não é possível fazer transferências entre a mesma conta.", thrown.getMessage());
        verify(contaRepository, times(1)).findSaldoById(anyLong());
    }

    @Test
    void testValidaTransferenciaComErro_SaldoInsuficiente() {
        ContaCorrente contaOrigem = new ContaCorrente();
        contaOrigem.setNumContaCorrente(1L);
        contaOrigem.setSaldo(10.0);
        transferencia.setContaCorrenteOrigem(contaOrigem);

        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setNumContaCorrente(2L);
        contaDestino.setSaldo(100.0);
        transferencia.setContaCorrenteDestino(contaDestino);

        when(contaRepository.findSaldoById(anyLong()))
                .thenThrow(new TransferenciaInvalidaException("Saldo insuficiente."));

        TransferenciaInvalidaException thrown = assertThrows(
                TransferenciaInvalidaException.class,
                () -> transferenciaService.validaTransferencia(transferencia)
        );

        assertEquals("Saldo insuficiente.", thrown.getMessage());
        verify(contaRepository, times(1)).findSaldoById(anyLong());
    }

    @Test
    void testValidaTransferenciaComErro_LimiteExcedido() {
        ContaCorrente contaOrigem = new ContaCorrente();
        contaOrigem.setNumContaCorrente(1L);
        contaOrigem.setSaldo(100.0);
        transferencia.setContaCorrenteOrigem(contaOrigem);

        ContaCorrente contaDestino = new ContaCorrente();
        contaDestino.setNumContaCorrente(2L);
        contaDestino.setSaldo(100.0);
        transferencia.setContaCorrenteDestino(contaDestino);

        transferencia.setValorTransferencia(1000.0);

        TransferenciaInvalidaException thrown = assertThrows(
                TransferenciaInvalidaException.class,
                () -> transferenciaService.validaTransferencia(transferencia)
        );

        assertEquals("Valor transferido excede o limite permitido.", thrown.getMessage());
    }

    //REALIZA TRANSFERENCIA
    @Test
    void testRealizarTransferenciaComSucesso() {
        Transferencia transferencia = new Transferencia();
        transferencia.setValorTransferencia(100.0);

        ContaCorrente origem = new ContaCorrente();
        origem.setNumContaCorrente(1L);
        origem.setSaldo(500.0);

        ContaCorrente destino = new ContaCorrente();
        destino.setNumContaCorrente(2L);
        destino.setSaldo(300.0);

        transferencia.setContaCorrenteOrigem(origem);
        transferencia.setContaCorrenteDestino(destino);

        when(contaService.buscarPorId(1L)).thenReturn(origem);
        when(contaService.buscarPorId(2L)).thenReturn(destino);

        transferenciaService.realizarTransferencia(transferencia);

        assertEquals(400.0, origem.getSaldo());
        assertEquals(400.0, destino.getSaldo());

        verify(contaRepository).save(origem);
        verify(contaRepository).save(destino);
        verify(transferenciaRepository, never()).save(transferencia); // pois não deu erro
    }

    @Test
    void testRealizarTransferenciaComNullPointerException() {
        Transferencia transferencia = new Transferencia();
        transferencia.setValorTransferencia(100.0);
        transferencia.setContaCorrenteOrigem(null);

        NullPointerException thrown = assertThrows(
                NullPointerException.class,
                () -> transferenciaService.realizarTransferencia(transferencia)
        );

        assertEquals(StatusTransferencia.FALHA, transferencia.getStatusTransferencia());
        verify(transferenciaRepository).save(transferencia);
    }

}
