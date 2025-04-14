package br.com.banco.consignadofgts_isabellebandeira.model;

import br.com.banco.consignadofgts_isabellebandeira.enums.StatusTransferencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transferencia")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransferencia;
    private Double valorTransferencia;
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

    //Construtor TransferenciaResponseDTO
    public Transferencia(Long idTransferencia, Double valorTransferencia, String statusTransferencia, LocalDateTime dataHoraTransferencia) {
        this.idTransferencia = idTransferencia;
        this.valorTransferencia = valorTransferencia;
        this.statusTransferencia = StatusTransferencia.valueOf(statusTransferencia);
        this.dataHoraTransferencia = dataHoraTransferencia;
    }

    //Construtor TransferenciaRequestDTO
    public Transferencia(Double valorTransferencia, ContaCorrente idContaOrigem, ContaCorrente idContaDestino) {
        this.valorTransferencia = valorTransferencia;
        this.contaCorrenteOrigem = new ContaCorrente(idContaOrigem.getNumContaCorrente(), idContaOrigem.getSaldo());
        this.contaCorrenteDestino = new ContaCorrente(idContaDestino.getNumContaCorrente(), idContaDestino.getSaldo());
    }
}
