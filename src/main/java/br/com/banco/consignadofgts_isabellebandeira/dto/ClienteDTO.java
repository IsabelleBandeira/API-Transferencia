package br.com.banco.consignadofgts_isabellebandeira.dto;

import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClienteDTO {
    private Long id;
    private ContaCorrenteDTO numContaCorrente;

    public Cliente toDomain(Long id, ContaCorrenteDTO numContaCorrente){
        return new Cliente(id, numContaCorrente);
    }
}
