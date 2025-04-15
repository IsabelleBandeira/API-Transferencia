package br.com.banco.consignadofgts_isabellebandeira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "tb_cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private String nome;

    @OneToOne
    @JoinColumn(name = "num_contacorrente", referencedColumnName = "numContaCorrente")
    private ContaCorrente numContacorrente;

    //Contrustor ClienteDTO
    public Cliente(String nome, ContaCorrente contaCorrente) {
        this.nome = nome;
        this.numContacorrente = contaCorrente;
    }

}
