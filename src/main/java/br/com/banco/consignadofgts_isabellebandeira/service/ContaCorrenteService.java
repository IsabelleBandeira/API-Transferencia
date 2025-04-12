package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaCorrenteService {
    private final ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    public ContaCorrenteService(ContaCorrenteRepository contaCorrenteRepository) {
        this.contaCorrenteRepository = contaCorrenteRepository;
    }

    public List<ContaCorrente> listarTodasAsContas(){
        return contaCorrenteRepository.findAll();
    }

    public Optional<ContaCorrente> buscarPorId(Long id){
        return contaCorrenteRepository.findById(id);
    }

    public ContaCorrente cadastrarContaCorrente(ContaCorrente contaCorrente){
        return contaCorrenteRepository.save(contaCorrente);
    }

}
