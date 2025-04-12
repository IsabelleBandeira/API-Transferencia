package br.com.banco.consignadofgts_isabellebandeira.repository;

import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
