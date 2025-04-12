package br.com.banco.consignadofgts_isabellebandeira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_conta_corrente")
public class ContaCorrente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numContaCorrente;
    private Double saldo;
}
