package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ContaCorrenteDTO {
    private Long numContaCorrente;
    private Double saldo;

    public ContaCorrente toDomain(){
        return new ContaCorrente(numContaCorrente, saldo);
    }
}
