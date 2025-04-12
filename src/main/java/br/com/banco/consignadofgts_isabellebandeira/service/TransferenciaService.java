package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.enums.StatusTransferencia;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import br.com.banco.consignadofgts_isabellebandeira.repository.TransferenciaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Service
public class TransferenciaService {
    private final TransferenciaRepository transferenciaRepository;
    private final ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    public TransferenciaService(TransferenciaRepository transferenciaRepository, ContaCorrenteRepository contaCorrenteRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.contaCorrenteRepository = contaCorrenteRepository;
    }

    public List<Transferencia> listarTodasAsTransferencias(){
        return transferenciaRepository.findAll();
    }

    public Optional<Transferencia> buscarPorId(Long id){
        return transferenciaRepository.findById(id);
//                .orElseThrow(() -> new TransferenciaNaoEncontradaException("Transferência não encontrada"));
    }

    public Optional<Transferencia> buscarPorContaCorrente(Long id){
        return transferenciaRepository.findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(id, id);
//             .orElseThrow(() -> new TransferenciaNaoEncontradaException("Transferência não encontrada"));
    }

    @Transactional
    public void cadastrarTransferencia(Transferencia transferencia){
        transferenciaRepository.save(transferencia);
    }

    public boolean validarSaldoContaOrigem(Transferencia transferencia){
        ContaCorrente contaCorrente = transferencia.getContaCorrenteOrigem();
        return contaCorrenteRepository.findSaldoById(contaCorrente.getNumContaCorrente()) >= transferencia.getValorTransferencia();
    }

    public boolean validarLimite(Transferencia transferencia){
        return transferencia.getValorTransferencia() <= 100.00;
    }

    public void atualizarStatusTransferencia(Transferencia transferencia){
        if (!validarLimite(transferencia)) {
            transferencia.setStatusTransferencia(StatusTransferencia.FALHA);
            transferenciaRepository.save(transferencia);
            throw new RuntimeException("Valor transferido excede o limite permitido.");
        }
        if (!validarSaldoContaOrigem(transferencia)) {
            transferencia.setStatusTransferencia(StatusTransferencia.FALHA);
            transferenciaRepository.save(transferencia);
            throw new RuntimeException("Saldo insuficiente.");
        }
    }

    @Transactional
    public void realizarTransferencia(Transferencia transferencia){
        Double valor = transferencia.getValorTransferencia();
        ContaCorrente contaCorrenteOrigem = transferencia.getContaCorrenteOrigem();
        ContaCorrente contaCorrenteDestino = transferencia.getContaCorrenteDestino();

        contaCorrenteOrigem.setSaldo(contaCorrenteOrigem.getSaldo() - valor);
        contaCorrenteDestino.setSaldo(contaCorrenteDestino.getSaldo() + valor);

        contaCorrenteRepository.save(contaCorrenteOrigem);
        contaCorrenteRepository.save(contaCorrenteDestino);
    }

    @Transactional
    public void finalizarTransferencia(Transferencia transferencia) {
        transferencia.setStatusTransferencia(StatusTransferencia.FINALIZADO);
        transferenciaRepository.save(transferencia);
    }

}
