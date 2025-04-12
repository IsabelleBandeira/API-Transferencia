package br.com.banco.consignadofgts_isabellebandeira.repository;

import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long> {
}
