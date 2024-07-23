package br.com.jbst.repositories;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jbst.entities.map.Contato;

public interface ContatoRepository extends JpaRepository<Contato, UUID> {
	

}
