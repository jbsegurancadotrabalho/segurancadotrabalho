package br.com.jbst.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.Turmas;

public interface TurmasRepository extends JpaRepository<Turmas, UUID >{

	@Query(
			"select t from Turmas t "

	)
	List<Turmas> findAllByDescricao();
	
	
	@Query(
			"select t from Turmas t "
		  + "left join t.instrutores i "
		  + "where t.id = :id "
	)
	Turmas find(@Param("id") UUID id);
	
	
	@Query("select t from Turmas t where t.id = :id ")
	Turmas findIdTurmas(@Param("id") UUID id);
	
	@Query("SELECT MAX(t.numeroTurma) FROM Turmas t")
    Integer findMaxNumeroTurmas();
	
	

    @Query("SELECT t FROM Turmas t")
	List<Turmas> findAllTurmas();
}
