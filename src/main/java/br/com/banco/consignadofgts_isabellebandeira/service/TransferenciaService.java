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

    //Lista todas as transferências cadastradas no banco de dados e lança exceção se não encontra nenhuma
    public List<Transferencia> listarTodasAsTransferencias(){
        List<Transferencia> listaTransferencia = transferenciaRepository.findAll();
        if (listaTransferencia.isEmpty()) {
            throw new TransferenciaNaoEncontradaException("Não há transferências cadastrados no momento.");
        }
        return listaTransferencia;
    }

    //Busca tranferência pelo seu ID e lança exceção se não encontra nenhuma
    public Transferencia buscarPorId(Long id){
        return transferenciaRepository.findByIdTransferencia(id)
                .orElseThrow(() -> new TransferenciaNaoEncontradaException("Não foi encontrada nenhuma transferência com esse id."));
    }

    //Busca tranferência pelo número da conta-corrente associada a ela e lança exceção se não encontra nenhuma
    public List<Transferencia> buscarPorContaCorrente(ContaCorrente contaCorrente){
        return transferenciaRepository.findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(contaCorrente, contaCorrente)
                .orElseThrow(() -> new TransferenciaNaoEncontradaException("Transferência não encontrada"));
    }

    //Cadastra uma nova transferência no banco de dados e dá rollback se inserção falhar
    @Transactional
    public Transferencia cadastrarTransferencia(Transferencia transferencia){
        return transferenciaRepository.save(transferencia);
    }

    //Valida se saldo da conta-corrente de origem é suficiente para aquela transferência
    public boolean validarSaldoContaOrigem(Transferencia transferencia){
        ContaCorrente contaCorrente = transferencia.getContaCorrenteOrigem();
        return contaCorrenteRepository
                .findSaldoById(contaCorrente.getNumContaCorrente()) >= transferencia.getValorTransferencia();
    }

    //Valida se valor transferido é menor ou igual ao limite
    public boolean validarLimite(Transferencia transferencia){
        return transferencia.getValorTransferencia() <= 100.00;
    }

    //Chama os métodos de validação e caso dê erro, salva status da transferência como FALHA e lança exceção
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

    //Busca as contas associadas à transferência e concretiza a transação, dando rollback em caso de falha
    @Transactional
    public void realizarTransferencia(Transferencia transferencia){
        try{
            //Busca valor que se deseja transferir
            Double valor = transferencia.getValorTransferencia();

            //Busca as contas de Origem e Destino
            ContaCorrente contaCorrenteOrigem = contaCorrenteService.buscarPorId(transferencia.getContaCorrenteOrigem().getNumContaCorrente());
            ContaCorrente contaCorrenteDestino = contaCorrenteService.buscarPorId(transferencia.getContaCorrenteDestino().getNumContaCorrente());

            //Tira valor transferido da conta de Origem e acrescenta à de destino
            contaCorrenteOrigem.setSaldo(contaCorrenteOrigem.getSaldo() - valor);
            contaCorrenteDestino.setSaldo(contaCorrenteDestino.getSaldo() + valor);

            //Salva a alteração no saldo das contas no banco de dados
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

    //Atualiza o status da transferência para FINALIZADO e lança exceção em caso de falha
    @Transactional
    public Transferencia finalizarTransferencia(Transferencia transferencia) {
        transferencia.setStatusTransferencia(StatusTransferencia.FINALIZADO);
        return transferenciaRepository.save(transferencia);
    }

}
