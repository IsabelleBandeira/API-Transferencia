package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.ClienteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.service.ClienteService;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Gerencia rotas relacionadas ao Cliente. API na versão v1.
@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    private final ClienteService clienteService;
    private final ContaCorrenteService contaCorrenteService;

    public ClienteController(ClienteService clienteService, ContaCorrenteService contaCorrenteService) {
        this.clienteService = clienteService;
        this.contaCorrenteService = contaCorrenteService;
    }

    // POST - Endpoint para cadastrar um cliente
    //Metodo recebe um JSON e retorna 201 se com sucesso
    @PostMapping("/cadastracliente")
    public ResponseEntity<?> cadastrarCliente(@RequestBody @Valid ClienteDTO clienteDTO) {
        // Cria conta-corrente
        ContaCorrente contacorrente = contaCorrenteService.cadastrarContaCorrente();
        // Associa conta criada ao novo cliente
        Cliente cliente = clienteDTO.toDomain(contacorrente);
        // Salva novo cliente no banco de dados
        clienteService.cadastrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente cadastrado com sucesso.");
    }

    // GET - Endpoint para listar todos os clientes cadastrados no banco de dados
    //Metodo retorna 200 e um json com os clientes se com sucesso
    @GetMapping("/listaclientes")
    public ResponseEntity<?> listarClientes(){
        List<Cliente> listaClientes =  clienteService.listarTodosOsClientes();
        return ResponseEntity.ok().body(listaClientes);
    }

    // GET - Endpoint para pesquisar um cliente pelo número da sua conta-corrente
    //Metodo recebe o número da conta-corrente e retorna 200 e um JSON contendo os dados do cliente se com sucesso
    @GetMapping("/buscaporcontacorrente/")
    public ResponseEntity<Object> buscarClientePorContaCorrente(@RequestParam Long numContaCorrente){
        Cliente cliente = clienteService.buscarPorContaCorrente(numContaCorrente);
        return ResponseEntity.ok(cliente); // -> this could be DTO
    }

    // DELETE - Endpoint para deletar um cliente da base de dados
    //Metodo recebe o ID do cliente e retorna 200 se com sucesso
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id){
        clienteService.deletarCliente(id);
        return ResponseEntity.ok().body("Cliente deletado com sucesso.");
    }

    // POST - Endpoint para atualizar dados do cliente na base de dados
    //Metodo recebe um JSON com o ID e o nome do cliente e retorna 200 se com sucesso.
    @PostMapping("/atualizacliente")
    public ResponseEntity<String> atualizaCliente(@RequestBody @Valid ClienteDTO clienteDTO) {

        // Verifica se cliente existe na base de dados
        Cliente clienteExistente = clienteService.buscarPorId(clienteDTO.getIdCliente());
        // Altera o nome do cliente para o nome recebido pelo JSON
        clienteExistente.setNome(clienteDTO.getNome());
        // Salva as alterações no banco de dados
        clienteService.atualizarCliente(clienteExistente);

        return ResponseEntity.ok("Cliente atualizado com sucesso.");
    }


    // GET - Endpoint para pesquisar um cliente pelo seu ID
    //Metodo recebe parâmetro com o ID do cliente e retorna 200 se com sucesso
    @GetMapping("{id}")
    public ResponseEntity<?> buscaClientePorId(@PathVariable Long id){
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok().body(cliente);
    }

}
