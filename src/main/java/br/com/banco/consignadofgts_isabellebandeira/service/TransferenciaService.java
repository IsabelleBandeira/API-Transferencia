package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.enums.StatusTransferencia;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoEncontradoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.transferencia.TransferenciaInvalidaException;
import br.com.banco.consignadofgts_isabellebandeira.exception.transferencia.TransferenciaNaoEncontradaException;
import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import br.com.banco.consignadofgts_isabellebandeira.repository.TransferenciaRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Service
public class TransferenciaService {
    private final TransferenciaRepository transferenciaRepository;
    private final ContaCorrenteRepository contaCorrenteRepository;
    private final ContaCorrenteService contaCorrenteService;

    @Autowired
    public TransferenciaService(TransferenciaRepository transferenciaRepository, ContaCorrenteRepository contaCorrenteRepository, ContaCorrenteService contaCorrenteService) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaCorrenteRepository = contaCorrenteRepository;
        this.contaCorrenteService = contaCorrenteService;
    }

    public List<Transferencia> listarTodasAsTransferencias(){
        List<Transferencia> listaTransferencia = transferenciaRepository.findAll();
        if (listaTransferencia.isEmpty()) {
            throw new TransferenciaNaoEncontradaException("Não há transferências cadastrados no momento.");
        }
        return listaTransferencia;
    }

    public Transferencia buscarPorId(Long id){
        return transferenciaRepository.findByIdTransferencia(id)
                .orElseThrow(() -> new TransferenciaNaoEncontradaException("Não foi encontrada nenhuma transferência com esse id."));
    }

    public List<Transferencia> buscarPorContaCorrente(ContaCorrente contaCorrente){
        return transferenciaRepository.findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(contaCorrente, contaCorrente)
                .orElseThrow(() -> new TransferenciaNaoEncontradaException("Transferência não encontrada"));
    }

    @Transactional
    public Transferencia cadastrarTransferencia(Transferencia transferencia){
        return transferenciaRepository.save(transferencia);
    }

    public boolean validarSaldoContaOrigem(Transferencia transferencia){
        ContaCorrente contaCorrente = transferencia.getContaCorrenteOrigem();
        return contaCorrenteRepository
                .findSaldoById(contaCorrente.getNumContaCorrente()) >= transferencia.getValorTransferencia();
    }

    public boolean validarLimite(Transferencia transferencia){
        return transferencia.getValorTransferencia() <= 100.00;
    }

    public Transferencia validaTransferencia(Transferencia transferencia){
        if (!validarLimite(transferencia)) {
            transferencia.setStatusTransferencia(StatusTransferencia.FALHA);
            transferenciaRepository.save(transferencia);
            throw new TransferenciaInvalidaException("Valor transferido excede o limite permitido.");
        }
        if (!validarSaldoContaOrigem(transferencia)) {
            transferencia.setStatusTransferencia(StatusTransferencia.FALHA);
            transferenciaRepository.save(transferencia);
            throw new TransferenciaInvalidaException("Saldo insuficiente.");
        }
        if (Objects.equals(transferencia.getContaCorrenteOrigem().getNumContaCorrente(), transferencia.getContaCorrenteDestino().getNumContaCorrente())) {
            transferencia.setStatusTransferencia(StatusTransferencia.FALHA);
            transferenciaRepository.save(transferencia);
            throw new TransferenciaInvalidaException("Não é permitido fazer transferências entre uma mesma conta.");
        }
        return transferencia;
    }

    @Transactional
    public void realizarTransferencia(Transferencia transferencia){
        try{
            Double valor = transferencia.getValorTransferencia();

            ContaCorrente contaCorrenteOrigem = contaCorrenteService.buscarPorId(transferencia.getContaCorrenteOrigem().getNumContaCorrente());
            ContaCorrente contaCorrenteDestino = contaCorrenteService.buscarPorId(transferencia.getContaCorrenteDestino().getNumContaCorrente());

            contaCorrenteOrigem.setSaldo(contaCorrenteOrigem.getSaldo() - valor);
            contaCorrenteDestino.setSaldo(contaCorrenteDestino.getSaldo() + valor);

            contaCorrenteRepository.save(contaCorrenteOrigem);
            contaCorrenteRepository.save(contaCorrenteDestino);
        } catch (NullPointerException e){
            transferencia.setStatusTransferencia(StatusTransferencia.FALHA);
            transferenciaRepository.save(transferencia);
            throw new NullPointerException(e.getMessage());
        } catch (Exception e) {
            transferencia.setStatusTransferencia(StatusTransferencia.FALHA);
            transferenciaRepository.save(transferencia);
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public Transferencia finalizarTransferencia(Transferencia transferencia) {
        transferencia.setStatusTransferencia(StatusTransferencia.FINALIZADO);
        return transferenciaRepository.save(transferencia);
    }

}
