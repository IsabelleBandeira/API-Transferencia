package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.ClienteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.service.ClienteService;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    private final ClienteService clienteService;
    private final ContaCorrenteService contaCorrenteService;

    public ClienteController(ClienteService clienteService, ContaCorrenteService contaCorrenteService) {
        this.clienteService = clienteService;
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping("/cadastracliente")
    public ResponseEntity<?> cadastrarCliente(@RequestBody ClienteDTO clienteDTO) {
        ContaCorrente contacorrente = contaCorrenteService.cadastrarContaCorrente();
        Cliente cliente = clienteDTO.toDomain(contacorrente);
        clienteService.cadastrarCliente(cliente);
        return ResponseEntity.ok("Cliente cadastrado com sucesso.");
    }

    @GetMapping("/listaclientes")
    public ResponseEntity<?> listarClientes(){
        List<Cliente> listaClientes =  clienteService.listarTodosOsClientes();
        return ResponseEntity.ok().body(listaClientes);
    }

    @GetMapping("/buscaporcontacorrente/")
    public ResponseEntity<Object> buscarClientePorContaCorrente(@RequestParam Long numContaCorrente){
        Cliente cliente = clienteService.buscarPorContaCorrente(numContaCorrente);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id){
        clienteService.deletarCliente(id);
        return ResponseEntity.ok().body("Cliente deletado com sucesso.");
    }

    @PostMapping("/atualizacliente/{id}")
    public ResponseEntity<?> atualizaCliente(@PathVariable Long id){
        Cliente cliente = clienteService.buscarPorId(id);
        clienteService.atualizarCliente(cliente);
        return ResponseEntity.ok().body("Cliente atualizado com sucesso.");
    }

    @GetMapping("{id}")
    public ResponseEntity<?> buscaClientePorId(@PathVariable Long id){
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok().body(cliente);
    }

}
