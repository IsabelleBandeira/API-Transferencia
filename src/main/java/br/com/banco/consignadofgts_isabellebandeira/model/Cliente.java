package br.com.banco.consignadofgts_isabellebandeira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private String nome;

    @OneToOne
    @JoinColumn(name = "num_contacorrente", referencedColumnName = "num_contacorrente")
    private ContaCorrente num_contacorrente;
}
