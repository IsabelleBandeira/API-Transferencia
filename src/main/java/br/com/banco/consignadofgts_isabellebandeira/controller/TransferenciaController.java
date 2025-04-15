package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.TransferenciaRequestDTO;
import br.com.banco.consignadofgts_isabellebandeira.dto.TransferenciaResponseDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import br.com.banco.consignadofgts_isabellebandeira.service.TransferenciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//Gerencia rotas relacionadas à Transferencia. API na versão v1.
@RestController
@RequestMapping("/api/v1/transferencia")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;
    private final ContaCorrenteService contaCorrenteService;

    public TransferenciaController(TransferenciaService transferenciaService, ContaCorrenteService contaCorrenteService) {
        this.transferenciaService = transferenciaService;
        this.contaCorrenteService = contaCorrenteService;
    }

    // POST - Endpoint para realizar transferências entre contas
    @PostMapping()
    public ResponseEntity<?> transferir(@RequestBody @Valid TransferenciaRequestDTO transferenciaRequestDTO) {
        //Verifica a existência e busca ambas as contas associadas à transação
        ContaCorrente contaOrigem = contaCorrenteService.buscarPorId(transferenciaRequestDTO.getIdContaOrigem());
        ContaCorrente contaDestino = contaCorrenteService.buscarPorId(transferenciaRequestDTO.getIdContaDestino());

        // Converte transferenciaDTO para Transferencia
        Transferencia transferencia = transferenciaRequestDTO.toDomain(contaOrigem, contaDestino);

        //Cadastra a transação independente de ela ser concluída com sucesso ou falha.
        transferenciaService.cadastrarTransferencia(transferencia);
        //Verifica se transferência é válida
        transferenciaService.validaTransferencia(transferencia);
        //Realiza a transferência do saldo da conta de origem para a conta de destino
        transferenciaService.realizarTransferencia(transferencia);
        //Atualiza o status da transferência para FINALIZADO
        transferenciaService.finalizarTransferencia(transferencia);

        return ResponseEntity.ok().body("Transferência realizada com sucesso!");
    }

    // GET - Endpoint para buscar transferências associadas a uma conta por data em ordem decrescente
    //Metodo recebe parâmetro com o número da conta-corrente e retorna 200 e a lista com as transferências se com sucesso
    @GetMapping("/buscaporconta/")
    public ResponseEntity<Object> buscarTransferenciaPorContaCorrente(@RequestParam Long numContaCorrente){
        //Busca a conta-corrente pelo ID fornecido
        ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(numContaCorrente);

        //Busca as transações associadas àquela conta
        List<Transferencia> listaTransferencia = transferenciaService.buscarPorContaCorrente(contaCorrente);
        //Transforma lista em TransferenciaResponseDTO para mandar como resposta JSON para o endpoint
        List<TransferenciaResponseDTO> response = listaTransferencia.stream()
                .map(t -> new TransferenciaResponseDTO(
                        t.getIdTransferencia(),
                        t.getContaCorrenteOrigem(),
                        t.getContaCorrenteDestino(),
                        t.getValorTransferencia(),
                        t.getStatusTransferencia().name(),
                        t.getDataHoraTransferencia()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET - Endpoint para listar todas as transferências cadastradas no banco de dados
    //Metodo retorna 200 e a lista com as transferências em caso de sucesso
    @GetMapping("/listatransferencias")
    public ResponseEntity<?> listarTransferencias(){
        List<Transferencia> listaTransferencia =  transferenciaService.listarTodasAsTransferencias();
        List<TransferenciaResponseDTO> response = listaTransferencia.stream()
                .map(t -> new TransferenciaResponseDTO(
                        t.getIdTransferencia(),
                        t.getContaCorrenteOrigem(),
                        t.getContaCorrenteDestino(),
                        t.getValorTransferencia(),
                        t.getStatusTransferencia().name(),
                        t.getDataHoraTransferencia()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET - Endpoint para buscar uma transferência pelo seu ID
    //Metodo retorna 200 e a transferência buscada
    @GetMapping("{id}")
    public ResponseEntity<?> buscaTransferenciaPorId(@PathVariable Long id){
        Transferencia transferencia = transferenciaService.buscarPorId(id);
        return ResponseEntity.ok().body(transferencia);
    }
}
