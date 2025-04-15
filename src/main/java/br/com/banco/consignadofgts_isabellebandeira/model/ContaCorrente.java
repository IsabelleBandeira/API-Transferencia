package br.com.banco.consignadofgts_isabellebandeira.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_conta_corrente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContaCorrente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numContaCorrente;

    @Builder.Default
    private Double saldo = 0.0;

    @PrePersist
    protected void onCreate() {
        this.saldo = 0.0;
    }

    public ContaCorrente(Double saldo) {
        this.saldo = saldo;
    }

}
