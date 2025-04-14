package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransferenciaResponseDTO {
    private Long idTransferencia;
    private ContaCorrente idContaOrigem;
    private ContaCorrente idContaDestino;
    private Double valorTransferencia;
    private String statusTransferencia;
    private LocalDateTime dataHoraTransferencia;
}
