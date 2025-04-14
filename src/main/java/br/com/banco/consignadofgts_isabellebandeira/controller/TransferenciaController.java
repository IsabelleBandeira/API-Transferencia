package br.com.banco.consignadofgts_isabellebandeira.controller;

import br.com.banco.consignadofgts_isabellebandeira.dto.TransferenciaRequestDTO;
import br.com.banco.consignadofgts_isabellebandeira.dto.TransferenciaResponseDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import br.com.banco.consignadofgts_isabellebandeira.service.ContaCorrenteService;
import br.com.banco.consignadofgts_isabellebandeira.service.TransferenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transferencia")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;
    private final ContaCorrenteService contaCorrenteService;

    public TransferenciaController(TransferenciaService transferenciaService, ContaCorrenteService contaCorrenteService) {
        this.transferenciaService = transferenciaService;
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping()
    public ResponseEntity<?> transferir(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO) {
        try{
            Optional<ContaCorrente> contaOrigem = contaCorrenteService.buscarPorId(transferenciaRequestDTO.getIdContaOrigem());
            Optional<ContaCorrente> contaDestino = contaCorrenteService.buscarPorId(transferenciaRequestDTO.getIdContaDestino());
            Transferencia transferencia = transferenciaRequestDTO.toDomain(contaOrigem, contaDestino);
            transferenciaService.cadastrarTransferencia(transferencia);
            transferenciaService.atualizarStatusTransferencia(transferencia);
            transferenciaService.realizarTransferencia(transferencia);
            transferenciaService.finalizarTransferencia(transferencia);
            return ResponseEntity.ok().body("Transferência realizada com sucesso!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao realizar transferência: " + e.getMessage());
        }

    }

    @GetMapping("/buscaporconta/")
    public ResponseEntity<Object> buscarTransferenciaPorContaCorrente(@RequestParam Long numContaCorrente){
        try {
            ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(numContaCorrente)
                    .orElseThrow(() -> new RuntimeException("Conta corrente não encontrada."));
            Optional<List<Transferencia>> listaTransferencia = transferenciaService.buscarPorContaCorrente(contaCorrente);
            if (listaTransferencia.isPresent()) {
                List<TransferenciaResponseDTO> response = listaTransferencia.get().stream()
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
            } else {
                Map<String, String> response = Map.of("message", "Nenhuma transferência encontrada associada a essa conta corrente.");
                return ResponseEntity.status(404).body(response);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao buscar transferência: " + e.getMessage());
        }
    }

    @GetMapping("/listatransferencias")
    public ResponseEntity<?> listarTransferencias(){
        try{
            List<Transferencia> listaTransferencia =  transferenciaService.listarTodasAsTransferencias();
            if (!listaTransferencia.isEmpty()) {
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
            } else throw new RuntimeException("Não existem transações cadastradas no momento.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao listar transferências: " + e.getMessage());
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<?> buscaTransferenciaPorId(@PathVariable Long id){
        try{
            Transferencia transferencia = transferenciaService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Não foi encontrada nenhuma transferência com esse id."));
            return ResponseEntity.ok().body(transferencia);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao buscar transferência: " + e.getMessage());
        }
    }
}
