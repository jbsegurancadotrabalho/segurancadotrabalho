package br.com.jbst.repositories;

import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.jbst.entities.Instrutor;

public interface InstrutorRepository extends JpaRepository<Instrutor, UUID > {

	
	  @Query("SELECT COUNT(i) > 0 FROM Instrutor i WHERE i.instrutor = :nomeInstrutor")
	    boolean existsByNome(String nomeInstrutor);
	
	@Query("select i from Instrutor i where i.id = :id ")
	Instrutor find(@Param("id") UUID id);
	
	   boolean existsByRg(String rg);
	    
	    boolean existsByCpf(String cpf);
}
