package br.com.banco.consignadofgts_isabellebandeira.repository;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//Acesso ao banco de dados
@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long> {
    @Query("SELECT c.saldo FROM ContaCorrente c WHERE c.numContaCorrente = :id")
    Double findSaldoById(@Param("id") Long id);
}
