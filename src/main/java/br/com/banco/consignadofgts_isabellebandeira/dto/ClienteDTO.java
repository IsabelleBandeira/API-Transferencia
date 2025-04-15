package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ClienteDTO {

    @NotBlank
    @Pattern(
        regexp = "^[A-ZÀ-ÖØ-Ÿ][a-zà-öø-ÿ]+(?: [A-ZÀ-ÖØ-Ÿa-zà-öø-ÿ'-]+)+$",
        message = "Nome Inválido. Por favor digite seu nome completo."
    )
    private String nome;
    private Long idCliente;
    private ContaCorrente contaCorrente;

    //Conversão para acessar metodos da classe
    public Cliente toDomain(ContaCorrente contaCorrente) {
        return new Cliente(nome, contaCorrente);
    }

}
