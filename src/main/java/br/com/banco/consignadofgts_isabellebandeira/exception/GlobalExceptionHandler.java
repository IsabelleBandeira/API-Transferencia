package br.com.banco.consignadofgts_isabellebandeira.exception;

import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoAtualizadoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoCadastradoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoDeletadoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.cliente.ClienteNaoEncontradoException;
import br.com.banco.consignadofgts_isabellebandeira.exception.conta.ContaNaoAtualizadaException;
import br.com.banco.consignadofgts_isabellebandeira.exception.conta.ContaNaoEncontradaException;
import br.com.banco.consignadofgts_isabellebandeira.exception.transferencia.TransferenciaInvalidaException;
import br.com.banco.consignadofgts_isabellebandeira.exception.transferencia.TransferenciaNaoEncontradaException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro interno: " + ex.getMessage()));
    }

    @ExceptionHandler(ClienteNaoCadastradoException.class)
    public ResponseEntity<?> handleClienteNaoCadastrado(ClienteNaoCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao cadastrar cliente: " + ex.getMessage()));
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<?> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Erro ao buscar cliente: " + ex.getMessage()));
    }

    @ExceptionHandler(ClienteNaoDeletadoException.class)
    public ResponseEntity<?> handleClienteNaoDeletado(ClienteNaoDeletadoException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro ao deletar cliente: " + ex.getMessage()));
    }

    @ExceptionHandler(ClienteNaoAtualizadoException.class)
    public ResponseEntity<?> handleClienteNaoAtualizado(ClienteNaoAtualizadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Erro ao atualizar cliente: " + ex.getMessage()));
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<?> handleContaNaoEncontrada(ContaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Erro ao buscar conta: " + ex.getMessage()));
    }

    @ExceptionHandler(ContaNaoAtualizadaException.class)
    public ResponseEntity<?> handleContaNaoAtualizada(ContaNaoAtualizadaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Erro ao atualizar conta: " + ex.getMessage()));
    }

    @ExceptionHandler(TransferenciaNaoEncontradaException.class)
    public ResponseEntity<?> handleTransferenciaNaoEncontrada(TransferenciaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Erro ao procurar transferência: " + ex.getMessage()));
    }

    @ExceptionHandler(TransferenciaInvalidaException.class)
    public ResponseEntity<?> handleTransferenciaInvalida(TransferenciaInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("error", "Erro ao realizar transferência: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleErroValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(Map.of("Erro(s) de Validação identificado(s)", erros));
    }

}
