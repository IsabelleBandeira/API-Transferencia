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
    public ResponseEntity<?> transferir(@RequestBody @Valid TransferenciaRequestDTO transferenciaRequestDTO) {
        ContaCorrente contaOrigem = contaCorrenteService.buscarPorId(transferenciaRequestDTO.getIdContaOrigem());
        ContaCorrente contaDestino = contaCorrenteService.buscarPorId(transferenciaRequestDTO.getIdContaDestino());
        Transferencia transferencia = transferenciaRequestDTO.toDomain(contaOrigem, contaDestino);
        transferenciaService.cadastrarTransferencia(transferencia);
        transferenciaService.validaTransferencia(transferencia);
        transferenciaService.realizarTransferencia(transferencia);
        transferenciaService.finalizarTransferencia(transferencia);
        return ResponseEntity.ok().body("Transferência realizada com sucesso!");
    }

    @GetMapping("/buscaporconta/")
    public ResponseEntity<Object> buscarTransferenciaPorContaCorrente(@RequestParam Long numContaCorrente){
        ContaCorrente contaCorrente = contaCorrenteService.buscarPorId(numContaCorrente);
        List<Transferencia> listaTransferencia = transferenciaService.buscarPorContaCorrente(contaCorrente);
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

    @GetMapping("{id}")
    public ResponseEntity<?> buscaTransferenciaPorId(@PathVariable Long id){
        Transferencia transferencia = transferenciaService.buscarPorId(id);
        return ResponseEntity.ok().body(transferencia);
    }
}
