package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import br.com.banco.consignadofgts_isabellebandeira.repository.TransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferenciaService {
    private final TransferenciaRepository transferenciaRepository;

    @Autowired
    public TransferenciaService(TransferenciaService transferenciaService, TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

    public List<Transferencia> listarTodasAsTransferencias(){
        return transferenciaRepository.findAll();
    }

    public Optional<Transferencia> buscarPorId(Long id){
        return transferenciaRepository.findById(id);
    }

    public Optional<Transferencia> buscarPorContaCorrente(Long id){
        return transferenciaRepository.findByContaCorrenteDestinoOrContaCorrenteOrigem(id, id);
    }

    public Transferencia cadastrarTransferencia(Transferencia transferencia){
        return transferenciaRepository.save(transferencia);
    }
}
