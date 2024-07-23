package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.map.Formacao;

public interface FormacaoRepository extends JpaRepository<Formacao, UUID > {

	
	@Query(
			"select f from Formacao f "
		  + "left join f.instrutores i "
		  + "where f.id = :id "
	)
	Formacao find(@Param("id") UUID id);
}
