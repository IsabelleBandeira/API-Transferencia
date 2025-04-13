package br.com.banco.consignadofgts_isabellebandeira.model;

import br.com.banco.consignadofgts_isabellebandeira.enums.StatusTransferencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @PrePersist
    protected void onCreate() {
        this.saldo = 0.0;
    }

    public ContaCorrente(Double saldo) {
        this.saldo = saldo;
    }

}
