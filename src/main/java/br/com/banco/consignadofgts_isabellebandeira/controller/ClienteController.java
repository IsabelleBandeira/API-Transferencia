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

    @GetMapping("/buscaporcontacorrente/")
    public ResponseEntity<Object> buscarClientePorContaCorrente(@RequestParam Long numContaCorrente){
        Optional<Cliente> cliente = clienteService.buscarPorContaCorrente(numContaCorrente);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            Map<String, String> response = Map.of("message", "Nenhum cliente encontrado com essa conta corrente.");
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id){
        try{
            clienteService.deletarCliente(id);
            return ResponseEntity.ok().body("Cliente deletado com sucesso.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao deletar cliente: " + e.getMessage());
        }
    }

    @PostMapping("/atualizacliente/{id}")
    public ResponseEntity<?> atualizaCliente(@PathVariable Long id){
        try{
            Cliente cliente = clienteService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
            clienteService.atualizarCliente(cliente);
            return ResponseEntity.ok().body("Cliente atualizado com sucesso.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao atualizar cliente: " + e.getMessage());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> buscaClientePorId(@PathVariable Long id){
        try{
            Cliente cliente = clienteService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
            return ResponseEntity.ok().body(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente: "+ e.getMessage());
        }
    }

}
