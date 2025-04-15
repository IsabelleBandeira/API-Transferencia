package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoAtualizadoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoEncontradoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.conta.ContaNaoAtualizadaException;
import br.com.banco.consignadofgts_isabellebandeira.exception.conta.ContaNaoEncontradaException;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaCorrenteService {
    private final ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    public ContaCorrenteService(ContaCorrenteRepository contaCorrenteRepository, ClienteService clienteService) {
        this.contaCorrenteRepository = contaCorrenteRepository;
    }

    public List<ContaCorrente> listarTodasAsContas(){
        return contaCorrenteRepository.findAll();
    }

    public ContaCorrente buscarPorId(Long id){
        return contaCorrenteRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta não encontrada."));
    }

    @Transactional
    public ContaCorrente cadastrarContaCorrente(){
        ContaCorrente contaCorrente = new ContaCorrente();
        return contaCorrenteRepository.save(contaCorrente);
    }

    @Transactional
    public ContaCorrente depositarSaldoContaCorrente(Double valor, ContaCorrente contaCorrente){
        try{
            contaCorrente.setSaldo(contaCorrente.getSaldo() + valor);
            return contaCorrenteRepository.save(contaCorrente);
        } catch (EmptyResultDataAccessException e){
            throw new ContaNaoEncontradaException("Conta não encontrada.");
        } catch (Exception e){
            throw new ContaNaoAtualizadaException(e.getMessage());
        }
    }

}
