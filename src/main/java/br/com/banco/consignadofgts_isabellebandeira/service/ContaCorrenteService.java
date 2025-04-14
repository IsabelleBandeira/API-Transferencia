package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaCorrenteService {
    private final ContaCorrenteRepository contaCorrenteRepository;
    private final ClienteService clienteService;

    @Autowired
    public ContaCorrenteService(ContaCorrenteRepository contaCorrenteRepository, ClienteService clienteService) {
        this.contaCorrenteRepository = contaCorrenteRepository;
        this.clienteService = clienteService;
    }

    public List<ContaCorrente> listarTodasAsContas(){
        return contaCorrenteRepository.findAll();
    }

    public Optional<ContaCorrente> buscarPorId(Long id){
        return contaCorrenteRepository.findById(id);
    }

    @Transactional
    public ContaCorrente cadastrarContaCorrente(){
        ContaCorrente contaCorrente = new ContaCorrente();
        return contaCorrenteRepository.save(contaCorrente);
    }

    @Transactional
    public Optional<ContaCorrente> atualizarSaldoContaCorrente(ContaCorrente contaCorrente){
        return Optional.of(contaCorrenteRepository.save(contaCorrente));
    }

}
