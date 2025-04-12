package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransferenciaRequestDTO {
    private Double valorTransferencia;
    private ContaCorrenteDTO idContaOrigem;
    private ContaCorrenteDTO idContaDestino;

    public Transferencia toDomain(){
        return new Transferencia(valorTransferencia, idContaOrigem, idContaDestino);
    }
}
