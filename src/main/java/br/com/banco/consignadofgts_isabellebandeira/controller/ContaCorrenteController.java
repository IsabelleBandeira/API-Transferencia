package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.ContaCorrenteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/conta-corrente")
public class ContaCorrenteController {

    private final ContaCorrenteService contaCorrenteService;

    public ContaCorrenteController(ContaCorrenteService contaCorrenteService) {
        this.contaCorrenteService = contaCorrenteService;
    }

    //mudar isso: so pode alterar o SALDO, n o id ou a conta em si
    @PostMapping("/alterarcontacorrente")
    public ResponseEntity<?> alterarContaCorrente(@RequestBody ContaCorrenteDTO contaCorrenteDTO){
        try{
            ContaCorrente contaCorrente = contaCorrenteDTO.toDomain();
            Optional<ContaCorrente> contaExiste = contaCorrenteService.buscarPorId(contaCorrente.getNumContaCorrente());
            if (contaExiste.isPresent()) {
                contaCorrenteService.atualizarSaldoContaCorrente(contaCorrente);
                return ResponseEntity.ok().body("Conta corrente alterada com sucesso.");
            } else throw new RuntimeException("Conta corrente não encontrada.");
        }catch (Exception e){ // e erro 404?
            return ResponseEntity.badRequest().body("Erro ao alterar conta corrente: " + e.getMessage());
        }
    }

    @GetMapping("/saldo/{id}")
    public ResponseEntity<?> buscarSaldo(@PathVariable Long id){
        try{
            Optional<ContaCorrente> contaCorrente = contaCorrenteService.buscarPorId(id);
            if(contaCorrente.isPresent()){
                return ResponseEntity.ok().body(contaCorrente.get());
            } else throw new RuntimeException("Conta não encontrada.");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao buscar saldo: " + e.getMessage());
        }
    }

    @GetMapping("/listacontas")
    public ResponseEntity<?> listarContas(){
        List<ContaCorrente> listaContas =  contaCorrenteService.listarTodasAsContas();
        return ResponseEntity.ok().body(listaContas);
    }

}
