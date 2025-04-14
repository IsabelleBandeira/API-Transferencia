package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClienteDTO {

    @NotNull
    @Pattern(
        regexp = "^[A-ZÀ-ÖØ-Ÿ][a-zà-öø-ÿ]+(?: [A-ZÀ-ÖØ-Ÿa-zà-öø-ÿ'-]+)+$",
        message = "Nome Inválido. Por favor digite seu nome completo."
    )
    private String nome;

    @NotNull
    @Positive
    private Long numContaCorrente;

    public Cliente toDomain(ContaCorrente contaCorrente) {
        return new Cliente(nome, contaCorrente);
    }
}
