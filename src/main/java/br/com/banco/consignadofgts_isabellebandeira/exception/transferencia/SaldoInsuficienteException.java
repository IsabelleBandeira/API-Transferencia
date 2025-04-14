package br.com.banco.consignadofgts_isabellebandeira.exception.transferencia;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
