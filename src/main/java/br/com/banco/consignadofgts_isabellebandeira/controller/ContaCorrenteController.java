package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.ContaCorrenteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conta-corrente")
public class ContaCorrenteController {

    private final ContaCorrenteService contaCorrenteService;

    public ContaCorrenteController(ContaCorrenteService contaCorrenteService) {
        this.contaCorrenteService = contaCorrenteService;
    }

    //mudar isso: so pode alterar o SALDO, n o id ou a conta em si
    @PostMapping("/alterarcontacorrente")
    public ResponseEntity<?> alterarContaCorrente(@RequestBody @Valid ContaCorrenteDTO contaCorrenteDTO){
        ContaCorrente contaCorrente = contaCorrenteDTO.toDomain();
        contaCorrenteService.atualizarSaldoContaCorrente(contaCorrente);
        return ResponseEntity.ok().body("Conta corrente atualizada com sucesso.");
    }

    @GetMapping("/saldo/{id}")
    public ResponseEntity<?> buscarSaldo(@PathVariable Long id){
        ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(id);
        return ResponseEntity.ok().body(contaCorrente);
    }

    @GetMapping("/listacontas")
    public ResponseEntity<?> listarContas(){
        List<ContaCorrente> listaContas =  contaCorrenteService.listarTodasAsContas();
        return ResponseEntity.ok().body(listaContas);
    }

}
