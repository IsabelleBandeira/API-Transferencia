package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ContaCorrenteDTO {

    @NotNull
    @Positive
    private Long numContaCorrente;

    @NotNull
    @Digits(integer = 3, fraction = 2)
    private Double saldo;

    public ContaCorrente toDomain(){
        return new ContaCorrente(numContaCorrente, saldo);
    }
}
