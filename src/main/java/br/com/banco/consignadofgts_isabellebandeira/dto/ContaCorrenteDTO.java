package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ContaCorrenteDTO {

    @NotNull
    @Positive
    private Long numContaCorrente;

    @NotNull
    @Digits(integer = 3, fraction = 2)
    private Double saldo;

    //Conversão para acessar os métodos da classe
    public ContaCorrente toDomain(){
        return new ContaCorrente(numContaCorrente, saldo);
    }
}
