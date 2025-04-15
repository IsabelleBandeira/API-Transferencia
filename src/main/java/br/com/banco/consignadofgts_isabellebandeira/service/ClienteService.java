package br.com.banco.consignadofgts_isabellebandeira.service;

import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoAtualizadoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoCadastradoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoDeletadoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoEncontradoException;
import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

//Acesso aos métodos com as regras de negócio
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //Lista todos os clientes do banco de dados e cria exceção se não houver nenhum
    public List<Cliente> listarTodosOsClientes(){
        List<Cliente> listaclientes = clienteRepository.findAll();
        if (listaclientes.isEmpty()) {
            throw new ClienteNaoEncontradoException("Não há clientes cadastrados no momento.");
        }
        return listaclientes;
    }

    //Busca um cliente no banco de dados pelo ID do cliente e lança exceção se não encontrar
    public Cliente buscarPorId(Long id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado."));
    }

    //Busca um cliente no banco de dados pelo número da Conta Corrente do cliente e lança exceção se não encontrar
    public Cliente buscarPorContaCorrente(Long id){
        return clienteRepository.findByNumContacorrente_NumContaCorrente(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Nenhum cliente encontrado com essa conta corrente."));
    }

    //Adiciona um cliente ao banco de dados e dá rollback se inserção falhar
    @Transactional
    public Cliente cadastrarCliente(Cliente cliente){
        try{
            return clienteRepository.save(cliente);
        } catch (Exception e){
            throw new ClienteNaoCadastradoException(e.getMessage());
        }
    }

    //Atualiza um registro cliente do banco, e dá rollback e lança exceção se falhar
    @Transactional
    public Cliente atualizarCliente(Cliente cliente){
        try{
            return clienteRepository.save(cliente);
        } catch (EmptyResultDataAccessException e){
            throw new ClienteNaoEncontradoException("Cliente não encontrado.");
        } catch (Exception e){
            throw new ClienteNaoAtualizadoException(e.getMessage());
        }
    }

    //Deleta um registro cliente do banco de dados, e dá rollback e lança exceção se falhar
    @Transactional
    public void deletarCliente(Long idCliente){
        try{
            clienteRepository.deleteById(idCliente);
        } catch (EmptyResultDataAccessException e) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado.");
        } catch (DataIntegrityViolationException e) {
            throw new ClienteNaoDeletadoException("Não foi possível deletar o cliente: Existem dados relacionados ao cliente.");
        } catch (Exception e) {
            throw new ClienteNaoDeletadoException("Erro inesperado ao deletar cliente: " + e.getMessage());
        }
    }

}
