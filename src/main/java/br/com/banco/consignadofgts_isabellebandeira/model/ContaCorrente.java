package br.com.banco.consignadofgts_isabellebandeira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_conta_corrente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaCorrente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numContaCorrente;
    private Double saldo;
}
