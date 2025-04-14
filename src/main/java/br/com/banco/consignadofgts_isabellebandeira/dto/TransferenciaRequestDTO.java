package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
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
    private Double valorTransferencia;
    private Long idContaOrigem;
    private Long idContaDestino;

    public Transferencia toDomain(Optional<ContaCorrente> contaOrigem, Optional<ContaCorrente> contaDestino) {
        return new Transferencia(
                valorTransferencia,
                contaOrigem.orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada.")),
                contaDestino.orElseThrow(() -> new IllegalArgumentException("Conta de destino não encontrada."))
        );
    }

}
