package br.com.jbst.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import br.com.jbst.entities.Faturamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FaturamentoRepository extends JpaRepository<Faturamento, UUID > {

	@Query("SELECT CASE WHEN COUNT(f.idfaturamento) > 0 THEN true ELSE false END " +
			 "FROM Faturamento f " +
			 "WHERE f.empresa.idEmpresa = :idEmpresa " +
			 "AND ((f.data_inicio >= :dataInicio AND f.data_inicio <= :dataFim) OR " +
			 "(f.data_fim >= :dataInicio AND f.data_fim <= :dataFim) OR " +
			 "(f.data_inicio <= :dataInicio AND f.data_fim >= :dataFim))")
			 
	
	boolean existsFaturamentoNoPeriodo(@Param("idEmpresa") UUID idEmpresa,
			 @Param("dataInicio") Instant dataInicio,
			 @Param("dataFim") Instant dataFim);

	
	
	 
	@Query("SELECT MAX(f.numeroFaturamento) FROM Faturamento f")
    Integer findMaxNumeroFaturamento();
	
    List<Faturamento> findByEmpresa_Usuario_Id(UUID idUsuario);

}
