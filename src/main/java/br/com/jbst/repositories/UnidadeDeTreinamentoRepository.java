package br.com.jbst.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jbst.entities.UnidadeDeTreinamento;

public interface UnidadeDeTreinamentoRepository extends JpaRepository<UnidadeDeTreinamento, UUID> {
	  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UnidadeDeTreinamento u WHERE u.cnpj = :cnpj")
	    boolean existsByCnpj(String cnpj);
}
