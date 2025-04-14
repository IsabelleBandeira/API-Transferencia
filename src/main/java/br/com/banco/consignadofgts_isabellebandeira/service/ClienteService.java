package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodosOsClientes(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id){
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorContaCorrente(Long id){
        return clienteRepository.findByNumContacorrente_NumContaCorrente(id);
    }

    @Transactional
    public Cliente cadastrarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Optional<Cliente> atualizarCliente(Cliente cliente){
        return Optional.of(clienteRepository.save(cliente));
    }

    @Transactional
    public void deletarCliente(Long idCliente){
        clienteRepository.deleteById(idCliente);
    }

}
