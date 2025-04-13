package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.ClienteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.service.ClienteService;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        try {
            ContaCorrente contacorrente = contaCorrenteService.cadastrarContaCorrente();
//            Cliente cliente = new Cliente(clienteDTO.getNome(), contacorrente.getNumContaCorrente());
            Cliente cliente = clienteDTO.toDomain(contacorrente);
            clienteService.cadastrarCliente(cliente);
            return ResponseEntity.ok("Cliente cadastrado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    @GetMapping("/listaclientes")
    public ResponseEntity<?> listarClientes(){
        List<Cliente> listaClientes =  clienteService.listarTodosOsClientes();
        return ResponseEntity.ok().body(listaClientes);
    }

    @GetMapping("/buscaporconta/")
    public ResponseEntity<Object> buscarClientePorContaCorrente(@RequestParam Long numContaCorrente){
        Optional<Cliente> cliente = clienteService.buscarPorContaCorrente(numContaCorrente);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            Map<String, String> response = Map.of("message", "Nenhum cliente encontrado com essa conta corrente.");
            return ResponseEntity.status(404).body(response);
        }
    }

}
