package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.ContaCorrenteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Gerencia rotas relacionadas à ContaCorrente. API na versão v1.
@RestController
@RequestMapping("/api/v1/conta-corrente")
public class ContaCorrenteController {

    private final ContaCorrenteService contaCorrenteService;

    public ContaCorrenteController(ContaCorrenteService contaCorrenteService) {
        this.contaCorrenteService = contaCorrenteService;
    }

    // POST - Endpoint para depositar um valor na conta-corrente
    //Metodo recebe um JSON e retorna 200 se com sucesso
    @PostMapping("/depositar")
    public ResponseEntity<?> depositarSaldo(@RequestBody @Valid ContaCorrenteDTO contaCorrenteDTO){
        ContaCorrente contaCorrenteRequest = contaCorrenteDTO.toDomain();
        //Busca o valor a ser depositado do JSON recebido
        Double valorDeposito = contaCorrenteRequest.getSaldo();
        //Verifica se a conta existe e a busca
        ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(contaCorrenteRequest.getNumContaCorrente());
        //Chama o metodo para realizar o depósito
        contaCorrenteService.depositarSaldoContaCorrente(valorDeposito, contaCorrente);
        return ResponseEntity.ok().body("Conta corrente atualizada com sucesso.");
    }

    // GET - Endpoint para buscar o saldo de uma conta-corrente baseado em seu ID (número da conta-corrente)
    //Metodo recebe um parâmetro com o ID da conta e retorna 200 se com sucesso
    @GetMapping("/saldo/{id}")
    public ResponseEntity<?> buscarSaldo(@PathVariable Long id){
        ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(id);
        return ResponseEntity.ok().body(contaCorrente);
    }

    //GET - Endpoint para listar todas as contas-corrente cadastradas no banco de dados
    //Metodo retorna 200 se com sucesso
    @GetMapping("/listacontas")
    public ResponseEntity<?> listarContas(){
        List<ContaCorrente> listaContas =  contaCorrenteService.listarTodasAsContas();
        return ResponseEntity.ok().body(listaContas);
    }

}
