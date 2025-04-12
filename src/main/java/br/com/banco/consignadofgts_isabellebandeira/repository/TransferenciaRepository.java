package br.com.banco.consignadofgts_isabellebandeira.repository;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    Optional<Transferencia> findByContaCorrenteDestinoOrContaCorrenteOrigemOrderByDataHoraTransferenciaDesc(Long contaCorrenteDestino, Long contaCorrenteOrigem);
}
