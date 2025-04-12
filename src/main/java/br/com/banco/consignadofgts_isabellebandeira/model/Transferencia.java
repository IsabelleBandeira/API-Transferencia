package br.com.banco.consignadofgts_isabellebandeira.model;

import br.com.banco.consignadofgts_isabellebandeira.enums.StatusTransferencia;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transferencia")
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransferencia;
    private Double valorTranferencia;
    private LocalDateTime dataHoraTransferencia;

    @Enumerated(EnumType.STRING)
    private StatusTransferencia statusTransferencia;

    @PrePersist
    protected void onCreate() {
        this.dataHoraTransferencia = LocalDateTime.now();
        this.statusTransferencia = StatusTransferencia.PROCESSANDO;
    }

    @ManyToOne
    @JoinColumn(name = "conta_corrente_origem", referencedColumnName = "numContaCorrente")
    private ContaCorrente contaCorrenteOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_corrente_destino", referencedColumnName = "numContaCorrente")
    private ContaCorrente contaCorrenteDestino;
}
