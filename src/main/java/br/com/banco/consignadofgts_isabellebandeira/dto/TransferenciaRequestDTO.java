package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransferenciaRequestDTO {

    @NotNull
    @Digits(integer = 3, fraction = 2)
    private Double valorTransferencia;

    @NotNull
    @Positive
    private Long idContaOrigem;

    @NotNull
    @Positive
    private Long idContaDestino;

    //Conversão para acesso aos metodos da classe
    public Transferencia toDomain(ContaCorrente contaOrigem, ContaCorrente contaDestino) {
        return new Transferencia(valorTransferencia, contaOrigem, contaDestino);
    }

}
